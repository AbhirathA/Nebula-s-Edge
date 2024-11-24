package com.spaceinvaders.backend;

import java.net.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spaceinvaders.backend.utils.Coordinate;

public class UDPClient {
    private static final String SERVER_ADDRESS = "172.16.224.45";
    private static final int SERVER_PORT = 9876;
    private static final int CLIENT_PORT = 9877; // Change this for each client
    private static final int BUFFER_SIZE = 10000;

    private DatagramSocket clientSocket = null;
    private InetAddress serverAddress = null;
    private final Gson gson = new Gson();

    public UDPClient() {
        try {
            this.clientSocket = new DatagramSocket(CLIENT_PORT);
            this.serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String data, String token) {
        try {
            String sendData = generateData(data, token);
            DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.length(), this.serverAddress, SERVER_PORT);
            this.clientSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Coordinate reviece(float x, float y, float angle) {
        try {
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            this.clientSocket.receive(receivePacket);

            String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
            return this.gson.fromJson(receivedData, Coordinate.class);

        } catch (Exception e) {
            e.printStackTrace();
            return new Coordinate("spaceship", x, y, angle);
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
