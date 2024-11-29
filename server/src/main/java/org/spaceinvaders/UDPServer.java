package org.spaceinvaders;

import java.net.*;
import com.google.gson.Gson;
import org.spaceinvaders.gameEngine.GameEngine;
import org.spaceinvaders.util.Coordinate;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.ServerInfo;
import org.spaceinvaders.util.UDPPacket;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * UDPServer.java
 * <br>
 * The UDPServer class handles communication between the game server and clients via UDP.
 * It receives state updates from clients, processes game logic, and sends game state updates
 * back to clients at a regular interval. The server operates in two separate threads:
 * one for network communication and one for game logic.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/28/2024
 */
public class UDPServer {
    private static final int BUFFER_SIZE = 10000;

    private final Gson gson;
    private final Map<InetSocketAddress, UDPPacket> inetSocketAddressUDPPacket;
    private final Map<InetSocketAddress, Integer> inetSocketAddressToId;
    private final Map<InetSocketAddress, Integer> inetSocketAddressToIdDead;
    private final Map<InetSocketAddress, UDPPacket> inetSocketAddressUDPPacketDead;
    private final Map<InetSocketAddress, String> inetSocketAddressToState;

    private final GameEngine gameEngine;

    private Thread gameLogicThread;
    private Thread networkThread;

    private int count = 0;

    private ArrayList<InetSocketAddress> validInetSocketAddresses;

    private AtomicBoolean inputBuffer, outputBuffer;

    private int port;

    /**
     * Constructs a new UDPServer instance, initializing necessary data structures
     * and the game engine.
     */
    public UDPServer(int port) {
        this.gson = new Gson();
        this.inetSocketAddressUDPPacket = new HashMap<>();
        this.inetSocketAddressToId = new HashMap<>();
        this.inetSocketAddressToIdDead = new HashMap<>();
        this.inetSocketAddressUDPPacketDead = new HashMap<>();
        this.inetSocketAddressToState = new HashMap<>();

        this.validInetSocketAddresses = new ArrayList<>();

        this.inputBuffer =  new AtomicBoolean(false);
        this.outputBuffer = new AtomicBoolean(false);

        this.port = port;

        this.gameEngine = new GameEngine();
    }

    /**
     * This method tells game engine to validate a new InetSocketAddress
     * @param inetSocketAddress
     */
    public void addInetSocketAddress(InetSocketAddress inetSocketAddress) {
        validInetSocketAddresses.add(inetSocketAddress);
    }

    /**
     * Starts the network and game logic threads. These threads handle network communication
     * and game state updates concurrently.
     */
    public void startThreads() {
        this.gameEngine.instantiateGameEngineObjects();
        this.networkThread = new NetworkThreadClass();
        this.gameLogicThread = new GameThreadClass();
        this.networkThread.start();
        this.gameLogicThread.start();
    }

    /**
     * GameThreadClass is responsible for processing game logic and updating the game state.
     * It listens for state changes from the clients and updates the game engine.
     */
    private class GameThreadClass extends Thread {
        @Override
        public void run() {
            HashMap<Integer, String> idToState = new HashMap<>();
            while (true) {
                // check if input is received
                if (UDPServer.this.inputBuffer.compareAndSet(true, false)) {
                    idToState = new HashMap<>();
                    synchronized (UDPServer.this.inetSocketAddressToState) {
                        synchronized (UDPServer.this.inetSocketAddressToId) {
                            // Process the state updates from clients
                            for (InetSocketAddress connection : UDPServer.this.inetSocketAddressToState.keySet()) {
                                if (UDPServer.this.inetSocketAddressToId.get(connection) == null) {
                                    int id = UDPServer.this.gameEngine.addShip();
                                    UDPServer.this.inetSocketAddressToId.put(connection, id);
                                }

                                idToState.put(UDPServer.this.inetSocketAddressToId.get(connection), UDPServer.this.inetSocketAddressToState.get(connection));
                            }
                        }
                    }
                }

                // Updating the game state based on received data
                for (int id : idToState.keySet()) {
                    UDPServer.this.gameEngine.updateState(id, idToState.get(id));
                }

                // Increment the game count (used for debugging)
                UDPServer.this.count++;
                System.out.println(count);
                UDPServer.this.gameEngine.update();

                // Prepare the game state data to send to clients
                UDPServer.this.gameEngine.getAllCoords();
                UDPPacket packet = new UDPPacket();
                packet.spaceShips = UDPServer.this.gameEngine.display("SHIP");
                packet.spaceShips.addAll(UDPServer.this.gameEngine.display("ENEMY"));
                packet.asteroids = UDPServer.this.gameEngine.display("ASTEROID");
                packet.asteroids.addAll(UDPServer.this.gameEngine.display("METEOR"));
                packet.bullets = UDPServer.this.gameEngine.display("BULLET");
                packet.blackholes = UDPServer.this.gameEngine.display("BLACKHOLES");
                packet.powerUpH = UDPServer.this.gameEngine.display("POWERUPH");
                packet.powerUpP = UDPServer.this.gameEngine.display("POWERUPP");
                packet.powerUpB = UDPServer.this.gameEngine.display("POWERUPB");

                // Checking if any ships have died
                synchronized (UDPServer.this.inetSocketAddressToState) {
                    synchronized (UDPServer.this.inetSocketAddressToId) {
                        for (InetSocketAddress connection : UDPServer.this.inetSocketAddressToId.keySet()) {
                            boolean flag = false;
                            for (Coordinate ship : packet.spaceShips) {
                                if (ship.id == UDPServer.this.inetSocketAddressToId.get(connection)) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                UDPServer.this.inetSocketAddressToIdDead.put(connection, UDPServer.this.inetSocketAddressToId.get(connection));
                                UDPServer.this.inetSocketAddressToId.remove(connection);
                                UDPServer.this.inetSocketAddressToState.remove(connection);
                            }
                        }
                    }
                }

                // Store the data for each client to send later
                synchronized (UDPServer.this.inetSocketAddressUDPPacket) {
                    for (InetSocketAddress connection : UDPServer.this.inetSocketAddressToId.keySet()) {
                        packet.id = UDPServer.this.inetSocketAddressToId.get(connection);
                        UDPServer.this.inetSocketAddressUDPPacket.put(connection, packet.clone());
                    }
                }
                synchronized (UDPServer.this.inetSocketAddressUDPPacketDead) {
                    for (InetSocketAddress connection : UDPServer.this.inetSocketAddressToIdDead.keySet()) {
                        packet.id = UDPServer.this.inetSocketAddressToIdDead.get(connection);
                        UDPServer.this.inetSocketAddressUDPPacketDead.put(connection, packet.clone());
                    }
                    UDPServer.this.inetSocketAddressToIdDead.clear();
                }
                UDPServer.this.outputBuffer.set(true);

                // Sleep to regulate the game loop
                try {
                    Thread.sleep(1000 / 90);
                } catch (Exception e) {
                    LoggerUtil.logError(e.getMessage());
                }
            }
        }
    }

    /**
     * NetworkThreadClass handles the communication with clients. It listens for incoming
     * UDP packets, processes the data, and sends updated game state back to the clients.
     */
    private class NetworkThreadClass extends Thread {
        @Override
        public void run() {
            try (DatagramSocket serverSocket = new DatagramSocket(UDPServer.this.port)) {
                LoggerUtil.logInfo("Server running on port " + UDPServer.this.port);

                byte[] receiveBuffer = new byte[BUFFER_SIZE];

                while (true) {
                    // Receive data from a client
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    serverSocket.receive(receivePacket);

                    // skip packet if an invalid packet is received
                    if (!UDPServer.this.validInetSocketAddresses.contains(new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort()))) continue;

                    InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());
                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // Deserialize the incoming data
                    Map<String, String> data = UDPServer.this.gson.fromJson(receivedData, Map.class);

                    // Retrieve the token and state from the data
                    String token = data.get("token");
                    String state = data.get("state");

                    synchronized (UDPServer.this.inetSocketAddressToState) {
                        // Map the client's address to the state
                        UDPServer.this.inetSocketAddressToState.put(clientAddress, state);
                    }

                    // Indicate that input has been received
                    UDPServer.this.inputBuffer.set(true);

                    // Check if the output buffer has data to send
                    if (UDPServer.this.outputBuffer.compareAndExchange(true, false)) {
                        // Prepare the data to send to clients
                        HashMap<InetSocketAddress, UDPPacket> sendDataTemp = new HashMap<>();
                        synchronized (UDPServer.this.inetSocketAddressUDPPacket) {
                            for (InetSocketAddress client : UDPServer.this.inetSocketAddressUDPPacket.keySet()) {
                                // Get the updated packet for each client
                                UDPPacket sendData = UDPServer.this.inetSocketAddressUDPPacket.get(client);
                                sendDataTemp.put(client, sendData);
                            }
                        }
                        synchronized (UDPServer.this.inetSocketAddressUDPPacketDead) {
                            for (InetSocketAddress client : UDPServer.this.inetSocketAddressUDPPacketDead.keySet()) {
                                // Get the updated packet for each client
                                UDPPacket sendData = UDPServer.this.inetSocketAddressUDPPacketDead.get(client);
                                sendDataTemp.put(client, sendData);
                            }
                            UDPServer.this.inetSocketAddressUDPPacketDead.clear();
                        }
                        // Send the data to the clients
                        for (InetSocketAddress client : sendDataTemp.keySet()) {
                            byte[] sendData = UDPServer.this.gson.toJson(sendDataTemp.get(client)).getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getAddress(), client.getPort());
                            serverSocket.send(sendPacket);
                        }
                    }
                }
            }
            catch (Exception e) {
                LoggerUtil.logError(e.getMessage());
            }
        }
    }
}
