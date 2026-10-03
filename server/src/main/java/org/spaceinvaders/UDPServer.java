package org.spaceinvaders;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.physics.Manager;
import org.spaceinvaders.gameEngine.GameEngine;
import org.spaceinvaders.util.Coordinate;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.UDPPacket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * UDPServer.java
 * <br>
 * The UDPServer class handles communication between the game server and clients via UDP.
 * It receives state updates from clients, processes game logic, and sends game state updates
 * back to clients at a regular interval. The server operates in two separate threads:
 * one for network communication and one for game logic.
 * <br>
 * All calls into the native physics engine happen on the game logic thread.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/28/2024
 */
public class UDPServer {
    private static final int BUFFER_SIZE = 10000;
    private static final int TICKS_PER_SECOND = 90;

    /** A single player world freezes while its player isn't sending input (e.g. on the pause screen). */
    private static final long PAUSE_AFTER_MS = 250;
    /** A single player world shuts down after this long without input. */
    private static final long SINGLE_PLAYER_IDLE_MS = TimeUnit.MINUTES.toMillis(5);
    /** A multiplayer ship is removed after its player has been silent this long. */
    private static final long MULTIPLAYER_IDLE_MS = TimeUnit.SECONDS.toMillis(60);
    /** How long a finished single player world keeps running so the final packet reaches the client. */
    private static final long GAME_OVER_LINGER_MS = 2000;

    private final Gson gson = new Gson();
    private final int requestedPort;
    private final boolean singlePlayer;

    private final Set<InetSocketAddress> validInetSocketAddresses = ConcurrentHashMap.newKeySet();

    // Shared between the two threads, guarded by stateLock
    private final Object stateLock = new Object();
    private final Map<InetSocketAddress, String> inetSocketAddressToState = new HashMap<>();
    private final Map<InetSocketAddress, Long> lastHeardFrom = new HashMap<>();
    private final Map<InetSocketAddress, UDPPacket> inetSocketAddressUDPPacket = new HashMap<>();
    private final Map<InetSocketAddress, UDPPacket> inetSocketAddressUDPPacketDead = new HashMap<>();

    private final AtomicBoolean outputBuffer = new AtomicBoolean(false);
    private volatile boolean running = false;
    private volatile long lastPacketTime;
    private volatile boolean heardFromPlayer = false;

    private DatagramSocket serverSocket;

    /**
     * Constructs a new UDPServer instance.
     * @param port         the UDP port to listen on, or 0 for any free port
     * @param singlePlayer whether this is a private world that should end with its player
     */
    public UDPServer(int port, boolean singlePlayer) {
        this.requestedPort = port;
        this.singlePlayer = singlePlayer;
    }

    /**
     * This method allows packets from the given client address
     * @param inetSocketAddress the address of the client's UDP socket
     */
    public void addInetSocketAddress(InetSocketAddress inetSocketAddress) {
        validInetSocketAddresses.add(inetSocketAddress);
    }

    /**
     * Returns the UDP port this world listens on (valid after {@link #startThreads()}).
     * @return the bound port
     */
    public int getPort() {
        return this.serverSocket != null ? this.serverSocket.getLocalPort() : this.requestedPort;
    }

    /**
     * Binds the UDP socket and starts the network and game logic threads. These threads
     * handle network communication and game state updates concurrently.
     * @throws SocketException if the port cannot be opened
     */
    public synchronized void startThreads() throws SocketException {
        if (this.running) return;
        this.serverSocket = new DatagramSocket(this.requestedPort);
        this.running = true;
        this.lastPacketTime = System.currentTimeMillis();

        Thread gameLogicThread = new GameThreadClass();
        Thread networkThread = new NetworkThreadClass();
        gameLogicThread.setName("game-" + getPort());
        networkThread.setName("udp-" + getPort());
        gameLogicThread.setDaemon(true);
        networkThread.setDaemon(true);
        networkThread.start();
        gameLogicThread.start();
    }

    private void shutdown() {
        LoggerUtil.logInfo("World on port " + getPort() + " shut down");
        this.running = false;
        this.serverSocket.close(); // unblocks receive() on the network thread
    }

    /**
     * GameThreadClass is responsible for processing game logic and updating the game state.
     * It listens for state changes from the clients and updates the game engine.
     */
    private class GameThreadClass extends Thread {
        private final Map<InetSocketAddress, Integer> inetSocketAddressToId = new HashMap<>();
        private final Set<InetSocketAddress> deadClients = new HashSet<>();
        private final Map<Integer, Coordinate> lastSeen = new HashMap<>();
        private long gameOverAt = -1;

        @Override
        public void run() {
            // The engine keeps its timers per thread, so the world is created and used only here
            GameEngine gameEngine = new GameEngine(Manager::new);
            try {
                gameEngine.instantiateGameEngineObjects();
                long tickNanos = TimeUnit.SECONDS.toNanos(1) / TICKS_PER_SECOND;
                long nextTick = System.nanoTime();
                while (UDPServer.this.running) {
                    tick(gameEngine);

                    // Sleep to regulate the game loop
                    nextTick += tickNanos;
                    long sleep = nextTick - System.nanoTime();
                    if (sleep > 0) {
                        TimeUnit.NANOSECONDS.sleep(sleep);
                    } else {
                        nextTick = System.nanoTime();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException e) {
                LoggerUtil.logException("Game loop on port " + getPort() + " crashed", e);
                shutdown();
            } finally {
                gameEngine.dispose();
            }
        }

        private void tick(GameEngine gameEngine) {
            long now = System.currentTimeMillis();

            if (UDPServer.this.singlePlayer) {
                if (gameOverAt >= 0 && now - gameOverAt > GAME_OVER_LINGER_MS
                        || now - UDPServer.this.lastPacketTime > SINGLE_PLAYER_IDLE_MS) {
                    shutdown();
                    return;
                }
                // Not started yet, or paused (the client stopped sending input): freeze the world
                if (!UDPServer.this.heardFromPlayer || now - UDPServer.this.lastPacketTime > PAUSE_AFTER_MS) return;
            }

            // Take a snapshot of the latest input from every client
            Map<InetSocketAddress, String> states;
            synchronized (UDPServer.this.stateLock) {
                states = new HashMap<>(UDPServer.this.inetSocketAddressToState);
                // A shot is applied once, not on every tick until the next packet arrives
                UDPServer.this.inetSocketAddressToState.replaceAll((client, state) -> state.replace("BULLET", ""));

                if (!UDPServer.this.singlePlayer) {
                    for (Map.Entry<InetSocketAddress, Long> heard : UDPServer.this.lastHeardFrom.entrySet()) {
                        Integer id = inetSocketAddressToId.get(heard.getKey());
                        if (id != null && now - heard.getValue() > MULTIPLAYER_IDLE_MS) {
                            LoggerUtil.logInfo("Removing idle player " + heard.getKey());
                            gameEngine.removeShip(id);
                        }
                    }
                }
            }

            // Process the state updates from clients, spawning a ship for new ones
            for (Map.Entry<InetSocketAddress, String> entry : states.entrySet()) {
                InetSocketAddress connection = entry.getKey();
                if (deadClients.contains(connection)) continue;
                Integer id = inetSocketAddressToId.get(connection);
                if (id == null) {
                    id = gameEngine.addShip();
                    inetSocketAddressToId.put(connection, id);
                    LoggerUtil.logInfo("Player " + connection + " joined port " + getPort() + " as ship " + id);
                }
                gameEngine.updateState(id, entry.getValue());
            }

            gameEngine.update();

            // Prepare the game state data to send to clients
            gameEngine.getAllCoords();
            UDPPacket packet = new UDPPacket();
            packet.spaceShips = gameEngine.display("SHIP");
            packet.enemies = gameEngine.display("ENEMY");
            packet.asteroids = gameEngine.display("ASTEROID");
            packet.asteroids.addAll(gameEngine.display("METEOR"));
            packet.bullets = gameEngine.display("BULLET");
            packet.blackholes = gameEngine.display("BLACKHOLES");
            packet.powerUpH = gameEngine.display("POWERUPH");
            packet.powerUpP = gameEngine.display("POWERUPP");
            packet.powerUpB = gameEngine.display("POWERUPB");

            // Checking if any ships have died
            Set<Integer> aliveShips = new HashSet<>();
            for (Coordinate ship : packet.spaceShips) {
                aliveShips.add(ship.id);
                lastSeen.put(ship.id, ship);
            }
            Map<InetSocketAddress, Integer> justDied = new HashMap<>();
            for (Iterator<Map.Entry<InetSocketAddress, Integer>> it = inetSocketAddressToId.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<InetSocketAddress, Integer> entry = it.next();
                if (!aliveShips.contains(entry.getValue())) {
                    Coordinate last = lastSeen.remove(entry.getValue());
                    LoggerUtil.logInfo("Ship " + entry.getValue() + " of " + entry.getKey() + " was destroyed"
                            + (last == null ? "" : " at (" + last.x + ", " + last.y + ") with " + last.health + " health left"));
                    justDied.put(entry.getKey(), entry.getValue());
                    deadClients.add(entry.getKey());
                    it.remove();
                }
            }
            if (UDPServer.this.singlePlayer && !justDied.isEmpty() && inetSocketAddressToId.isEmpty()) {
                gameOverAt = now;
            }

            // Store the data for each client to send later
            synchronized (UDPServer.this.stateLock) {
                for (Map.Entry<InetSocketAddress, Integer> entry : inetSocketAddressToId.entrySet()) {
                    packet.id = entry.getValue();
                    UDPServer.this.inetSocketAddressUDPPacket.put(entry.getKey(), packet.clone());
                }
                // Dead players get one last packet without their ship, so the client shows game over
                for (Map.Entry<InetSocketAddress, Integer> entry : justDied.entrySet()) {
                    packet.id = entry.getValue();
                    UDPServer.this.inetSocketAddressUDPPacket.remove(entry.getKey());
                    UDPServer.this.inetSocketAddressUDPPacketDead.put(entry.getKey(), packet.clone());
                    UDPServer.this.inetSocketAddressToState.remove(entry.getKey());
                    UDPServer.this.lastHeardFrom.remove(entry.getKey());
                }
            }
            UDPServer.this.outputBuffer.set(true);
        }
    }

    /**
     * NetworkThreadClass handles the communication with clients. It listens for incoming
     * UDP packets, processes the data, and sends updated game state back to the clients.
     */
    private class NetworkThreadClass extends Thread {
        @Override
        public void run() {
            LoggerUtil.logInfo("UDP world running on port " + getPort());
            byte[] receiveBuffer = new byte[BUFFER_SIZE];

            while (UDPServer.this.running) {
                try {
                    // Receive data from a client
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    UDPServer.this.serverSocket.receive(receivePacket);

                    // skip packet if it doesn't come from a client that completed the handshake
                    InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());
                    if (!UDPServer.this.validInetSocketAddresses.contains(clientAddress)) continue;

                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // Deserialize the incoming data
                    @SuppressWarnings("unchecked")
                    Map<String, String> data = UDPServer.this.gson.fromJson(receivedData, Map.class);
                    String state = data.get("state") != null ? data.get("state") : "";

                    UDPServer.this.lastPacketTime = System.currentTimeMillis();
                    UDPServer.this.heardFromPlayer = true;
                    synchronized (UDPServer.this.stateLock) {
                        // Keep an unprocessed shot if a newer packet arrives before the next tick
                        String previous = UDPServer.this.inetSocketAddressToState.get(clientAddress);
                        if (previous != null && previous.contains("BULLET") && !state.contains("BULLET")) {
                            state += "BULLET";
                        }
                        // Map the client's address to the state
                        UDPServer.this.inetSocketAddressToState.put(clientAddress, state);
                        UDPServer.this.lastHeardFrom.put(clientAddress, UDPServer.this.lastPacketTime);
                    }

                    // Check if the output buffer has data to send
                    if (UDPServer.this.outputBuffer.compareAndSet(true, false)) {
                        // Prepare the data to send to clients
                        Map<InetSocketAddress, UDPPacket> sendDataTemp;
                        synchronized (UDPServer.this.stateLock) {
                            sendDataTemp = new HashMap<>(UDPServer.this.inetSocketAddressUDPPacket);
                            sendDataTemp.putAll(UDPServer.this.inetSocketAddressUDPPacketDead);
                            UDPServer.this.inetSocketAddressUDPPacketDead.clear();
                        }
                        // Send the data to the clients
                        for (Map.Entry<InetSocketAddress, UDPPacket> client : sendDataTemp.entrySet()) {
                            byte[] sendData = UDPServer.this.gson.toJson(client.getValue()).getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getKey());
                            UDPServer.this.serverSocket.send(sendPacket);
                        }
                    }
                } catch (JsonSyntaxException | ClassCastException e) {
                    LoggerUtil.logError("Ignoring malformed packet: " + e.getMessage());
                } catch (Exception e) {
                    if (UDPServer.this.running) {
                        LoggerUtil.logException("UDP error on port " + getPort(), e);
                    }
                }
            }
        }
    }
}
