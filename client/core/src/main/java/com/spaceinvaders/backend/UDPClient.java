package com.spaceinvaders.backend;

import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spaceinvaders.backend.firebase.utils.ServerInfo;
import com.spaceinvaders.backend.utils.UDPPacket;

public class UDPClient {
    private static final int CLIENT_PORT = 9877; // Change this for each client
    private static final int BUFFER_SIZE = 10000;

    private DatagramSocket clientSocket = null;
    private InetAddress serverAddress = null;
    private final Gson gson = new Gson();

    private final UDPReceive udpReceive;
    public final UDPPacket udpPacket;
    public Thread receiveThread;
    public boolean isThreadRunning = false;

    private AtomicBoolean hasReceivedPacket = new AtomicBoolean(false);

    public UDPClient(UDPPacket udpPacket, AtomicBoolean hasReceivedPacket) {
        this.udpPacket = udpPacket;
        this.udpReceive = new UDPReceive();
        this.hasReceivedPacket = hasReceivedPacket;
        try {
            this.clientSocket = new DatagramSocket(CLIENT_PORT);
//            this.clientSocket.setSoTimeout(1000);
            this.serverAddress = InetAddress.getByName(ServerInfo.getIP());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String state, String token) {
        try {
            String sendData = generateData(state, token);
            DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), this.serverAddress, ServerInfo.getUdpPortMultiPlayer());
            this.clientSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReceiveThread() {
        if (this.isThreadRunning) {
            this.receiveThread.interrupt();
        }

        this.receiveThread = new Thread(this.udpReceive);
        this.receiveThread.start();
        this.isThreadRunning = true;
    }

    private class UDPReceive implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    byte[] receiveBuffer = new byte[BUFFER_SIZE];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    UDPClient.this.clientSocket.receive(receivePacket);
                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println(receivedData);

                    UDPClient.this.hasReceivedPacket.set(true);

                    synchronized (UDPClient.this.udpPacket) {
                        UDPClient.this.udpPacket.update(UDPClient.this.gson.fromJson(receivedData, UDPPacket.class));
                    }

                    if (Thread.interrupted()) {
                        UDPClient.this.isThreadRunning = false;
                        return;
                    }

                } catch (SocketTimeoutException se) {
                    System.out.println("Server connection timed out");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Simulates random data generation
    private static String generateData(String state, String token) {
        // Create a JsonObject
        JsonObject jsonObject = new JsonObject();

        // Add properties to the JsonObject
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("state", state);

        // Convert JsonObject to JSON string

        return jsonObject.toString();
    }
}
