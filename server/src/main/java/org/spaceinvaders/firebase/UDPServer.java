package org.spaceinvaders.firebase;

import java.net.*;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.A;
import org.spaceinvaders.firebase.util.Coordinate;
import org.spaceinvaders.firebase.util.UDPPacket;

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

        inputBuffer =  new AtomicBoolean(false);
        outputBuffer = new AtomicBoolean(false);
    }

    public void startThreads() {
        this.networkThread = new NetworkThreadClass();
        this.gameLogicThread = new GameThreadClass();
        this.networkThread.start();
        this.gameLogicThread.start();
    }

    private void moveInDirection(float speed, Coordinate coords) {
        double angleRad = Math.toRadians(coords.angle) + 1.571f;

        double deltaX = Math.cos(angleRad) * speed;
        double deltaY = Math.sin(angleRad) * speed;

        coords.x += (float) deltaX;
        coords.y += (float) deltaY;

        this.fixCoords(coords);
    }

    private void fixCoords(Coordinate coords) {
        coords.x = Math.min(Math.max(coords.x, 0), this.WORLD_WIDTH - 21);
        coords.y = Math.min(Math.max(0, coords.y), this.WORLD_HEIGHT - 21);
    }

    private class GameThreadClass extends Thread {
        @Override
        public void run() {

            while (true) {
                HashMap<Integer, String> idToState = new HashMap<>();
                ArrayList<String> newTokens = new ArrayList<>();
                synchronized (UDPServer.this.tokenToState) {
                    synchronized (UDPServer.this.tokenToId) {
                        for (String token : UDPServer.this.tokenToState.keySet()) {
                            if (tokenToId.get(token) == null)
                                newTokens.add(token);
                            else {
                                idToState.put(UDPServer.this.tokenToId.get(token), UDPServer.this.tokenToState.get(token));
                            }
                        }
                    }
                }

                // TODO: call create object and obtain new ids (new tokens are in newTokens) and add them to
                //  do the tokenToId map.

                // TODO: assume we get it back in the form of an ArrayList<Coordinate>, and I have to look at id
                //  and categorize them, and then add them to the outputBuffer and set its atomic variable to true
            }
        }
    }

    private class NetworkThreadClass extends Thread {
        @Override
        public void run() {
            try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT))
            {
                System.out.println("Server running on port " + SERVER_PORT);

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
                    if (UDPServer.this.outputBuffer.get()) {
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
                        }
                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // Simulates game engine
    private String processData(String receiveData) {
        // Deserialize JSON into a Map
        Map<String, String> data = this.gson.fromJson(receiveData, Map.class);

        // Retrieve the values
        String token = data.get("token");
        String state = data.get("state");

        // TODO: Implement JNI here
        // some computation
        if (state.contains("LEFT")) coords.angle += 1;
        else if (state.contains("RIGHT")) coords.angle -= 1;
        if (state.contains("FORWARD")) moveInDirection(1, coords);
        else if (state.contains("BACKWARD")) moveInDirection(-1, coords);

        // NOTE: You can return anything from which this class can infer the different coords of different objects
        // it can be in any form (the most preferred form this UDPPacket) but I can always convert any form to that :P

        return this.gson.toJson(new UDPPacket(this.coords));
    }
}