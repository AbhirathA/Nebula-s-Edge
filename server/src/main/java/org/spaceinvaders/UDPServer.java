package org.spaceinvaders;

import java.net.*;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.spaceinvaders.gameEngine.GameEngine;
import org.spaceinvaders.util.Coordinate;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.UDPPacket;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UDPServer
{
    private static final int SERVER_PORT = 9876;
    private static final int BUFFER_SIZE = 10000;

    private final float WORLD_WIDTH = 720;
    private final float WORLD_HEIGHT = 405;

    private final Coordinate coords;
    private final Gson gson;

    private final Map<InetSocketAddress, String> inetSocketAddressToToken;
    private final Map<String, UDPPacket> tokenToUdpPacket;
    private final Map<String, Integer> tokenToId;
    private final Map<String, String> tokenToState;

    private final GameEngine gameEngine;

    private Thread gameLogicThread;
    private Thread networkThread;

    private AtomicBoolean inputBuffer, outputBuffer;

    public UDPServer()
    {
        this.coords = new Coordinate("spaceship", -1, WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f, 0);
        this.gson = new Gson();
        this.inetSocketAddressToToken = new HashMap<>();
        this.tokenToUdpPacket = new HashMap<>();
        this.tokenToId = new HashMap<>();
        this.tokenToState = new HashMap<>();

        this.inputBuffer =  new AtomicBoolean(false);
        this.outputBuffer = new AtomicBoolean(false);

        this.gameEngine = new GameEngine();
    }

    public void startThreads() {
        this.networkThread = new NetworkThreadClass();
        this.gameLogicThread = new GameThreadClass();
        this.networkThread.start();
        this.gameLogicThread.start();
    }

    private class GameThreadClass extends Thread {
        @Override
        public void run() {
            while (true) {
                HashMap<Integer, String> idToState = new HashMap<>();
                synchronized (UDPServer.this.tokenToState) {
                    synchronized (UDPServer.this.tokenToId) {
                        for (String token : UDPServer.this.tokenToState.keySet()) {
                            if (tokenToId.get(token) == null) {
                                UDPServer.this.tokenToId.put(token, UDPServer.this.gameEngine.addElement("SHIP"));
                            }

                            idToState.put(UDPServer.this.tokenToId.get(token), UDPServer.this.tokenToState.get(token));
                        }
                    }
                }

                // updating states
                for (int id : idToState.keySet()) {
                    UDPServer.this.gameEngine.updateState(id, idToState.get(id));
                }

                // continue the game
                UDPServer.this.gameEngine.update();

                // time to send data to clients
                UDPPacket packet = new UDPPacket();
                packet.spaceShips = UDPServer.this.gameEngine.display("SHIP");
                packet.asteroids = UDPServer.this.gameEngine.display("ASTERIOD");
                packet.bullets = UDPServer.this.gameEngine.display("BULLET");
                packet.blackholes = UDPServer.this.gameEngine.display("BLACKHOLES");

                synchronized (UDPServer.this.tokenToUdpPacket) {
                    for (String token : UDPServer.this.tokenToId.keySet()) {
                        packet.id = UDPServer.this.tokenToId.get(token);
                        UDPServer.this.tokenToUdpPacket.put(token, packet.clone());
                    }
                }
                UDPServer.this.outputBuffer.set(true);

                // sleeping
                try {
                    Thread.sleep(1000 / 90);
                } catch (Exception e) {
                    LoggerUtil.logError(e.getMessage());
                }
            }
        }
    }

    private class NetworkThreadClass extends Thread {
        @Override
        public void run() {
            try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT))
            {
                LoggerUtil.logInfo("Server running on port " + SERVER_PORT);

                byte[] receiveBuffer = new byte[BUFFER_SIZE];

                while (true)
                {
                    // Receive data from a client
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    serverSocket.receive(receivePacket);

                    InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());
                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // Deserialize JSON into a Map
                    Map<String, String> data = UDPServer.this.gson.fromJson(receivedData, Map.class);

                    // Retrieve the values
                    String token = data.get("token");
                    String state = data.get("state");

                    synchronized (UDPServer.this.inetSocketAddressToToken) {
                        // Update the client's data
                        UDPServer.this.inetSocketAddressToToken.put(clientAddress, token);
                    }

                    synchronized (UDPServer.this.tokenToState) {
                        // Update the client's state
                        UDPServer.this.tokenToState.put(token, state);
                    }

                    // set input buffer to true
                    UDPServer.this.inputBuffer.set(true);

                    // check if output buffer is filled
                    if (UDPServer.this.outputBuffer.compareAndExchange(true, false)) {
                        // copy all data to a local variable
                        HashMap<InetSocketAddress, UDPPacket> sendDataTemp = new HashMap<>();
                        synchronized (UDPServer.this.inetSocketAddressToToken) {
                            synchronized (UDPServer.this.tokenToUdpPacket) {
                                for (InetSocketAddress client : UDPServer.this.inetSocketAddressToToken.keySet()) {
                                    // get the correct packet after a long process
                                    UDPPacket sendData = UDPServer.this.tokenToUdpPacket.get(UDPServer.this.inetSocketAddressToToken.get(client));
                                    sendDataTemp.put(client, sendData);
                                }
                            }
                        }
                        // unlock the variables and then send the data
                        for (InetSocketAddress client : sendDataTemp.keySet()) {
                            byte[] sendData = UDPServer.this.gson.toJson(sendDataTemp.get(client)).getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getAddress(), client.getPort());
                            serverSocket.send(sendPacket);
                            LoggerUtil.logInfo(UDPServer.this.gson.toJson(sendDataTemp.get(client)));
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