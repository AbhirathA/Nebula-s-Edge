package org.spaceinvaders;

import java.net.*;
import com.google.gson.Gson;
import org.spaceinvaders.gameEngine.GameEngine;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.ServerInfo;
import org.spaceinvaders.util.UDPPacket;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
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
public class UDPServer
{
    private static final int BUFFER_SIZE = 10000;

    private final Gson gson;
    private final Map<InetSocketAddress, String> inetSocketAddressToToken;
    private final Map<String, UDPPacket> tokenToUdpPacket;
    private final Map<String, Integer> tokenToId;
    private final Map<String, String> tokenToState;

    private final GameEngine gameEngine;

    private Thread gameLogicThread;
    private Thread networkThread;

    private int count = 0;

    private AtomicBoolean inputBuffer, outputBuffer;

    /**
     * Constructs a new UDPServer instance, initializing necessary data structures
     * and the game engine.
     */
    public UDPServer()
    {
        this.gson = new Gson();
        this.inetSocketAddressToToken = new HashMap<>();
        this.tokenToUdpPacket = new HashMap<>();
        this.tokenToId = new HashMap<>();
        this.tokenToState = new HashMap<>();

        this.inputBuffer =  new AtomicBoolean(false);
        this.outputBuffer = new AtomicBoolean(false);

        this.gameEngine = new GameEngine();
    }

    /**
     * Starts the network and game logic threads. These threads handle network communication
     * and game state updates concurrently.
     */
    public void startThreads() {
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
            while (true)
            {
                // check if input is received
                if (UDPServer.this.inputBuffer.compareAndSet(true, false))
                {
                    idToState = new HashMap<>();
                    synchronized (UDPServer.this.tokenToState) {
                        synchronized (UDPServer.this.tokenToId) {
                            // Process the state updates from clients
                            for (String token : UDPServer.this.tokenToState.keySet())
                            {
                                if (tokenToId.get(token) == null)
                                {
                                    int id = UDPServer.this.gameEngine.addShip();
                                    UDPServer.this.tokenToId.put(token, id);
                                    UDPServer.this.gameEngine.instantiateGameEngine(id);
                                }

                                idToState.put(UDPServer.this.tokenToId.get(token), UDPServer.this.tokenToState.get(token));
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
                packet.asteroids = UDPServer.this.gameEngine.display("ASTEROID");
                packet.bullets = UDPServer.this.gameEngine.display("BULLET");
                packet.blackholes = UDPServer.this.gameEngine.display("BLACKHOLES");

                // Store the data for each client to send later
                synchronized (UDPServer.this.tokenToUdpPacket) {
                    for (String token : UDPServer.this.tokenToId.keySet()) {
                        packet.id = UDPServer.this.tokenToId.get(token);
                        UDPServer.this.tokenToUdpPacket.put(token, packet.clone());
                    }
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
            try (DatagramSocket serverSocket = new DatagramSocket(ServerInfo.UDP_PORT))
            {
                LoggerUtil.logInfo("Server running on port " + ServerInfo.UDP_PORT);

                byte[] receiveBuffer = new byte[BUFFER_SIZE];

                while (true)
                {
                    // Receive data from a client
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    serverSocket.receive(receivePacket);

                    InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());
                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // Deserialize the incoming data
                    Map<String, String> data = UDPServer.this.gson.fromJson(receivedData, Map.class);

                    // Retrieve the token and state from the data
                    String token = data.get("token");
                    String state = data.get("state");

                    synchronized (UDPServer.this.inetSocketAddressToToken) {
                        // Map the client's address to the token
                        UDPServer.this.inetSocketAddressToToken.put(clientAddress, token);
                    }

                    synchronized (UDPServer.this.tokenToState) {
                        // Update the state of the client
                        UDPServer.this.tokenToState.put(token, state);
                    }

                    // Indicate that input has been received
                    UDPServer.this.inputBuffer.set(true);

                    // Check if the output buffer has data to send
                    if (UDPServer.this.outputBuffer.compareAndExchange(true, false)) {
                        // Prepare the data to send to clients
                        HashMap<InetSocketAddress, UDPPacket> sendDataTemp = new HashMap<>();
                        synchronized (UDPServer.this.inetSocketAddressToToken) {
                            synchronized (UDPServer.this.tokenToUdpPacket) {
                                for (InetSocketAddress client : UDPServer.this.inetSocketAddressToToken.keySet()) {
                                    // Get the updated packet for each client
                                    UDPPacket sendData = UDPServer.this.tokenToUdpPacket.get(UDPServer.this.inetSocketAddressToToken.get(client));
                                    sendDataTemp.put(client, sendData);
                                }
                            }
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
            catch (Exception e)
            {
                LoggerUtil.logError(e.getMessage());
            }
        }
    }
}
