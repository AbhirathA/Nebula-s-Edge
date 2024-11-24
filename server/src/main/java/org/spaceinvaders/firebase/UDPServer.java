package org.spaceinvaders.firebase;

import java.net.*;
import java.util.*;
import com.google.gson.Gson;
import org.spaceinvaders.firebase.util.Coordinate;

public class UDPServer {
    private static final int SERVER_PORT = 9876;
    private static final int BUFFER_SIZE = 10000;

    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;

    private final Coordinate coords;
    private final Gson gson;

    public UDPServer() {
        this.coords = new Coordinate("spaceship", WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f);
        this.gson = new Gson();
    }

    public void server() {
        Map<InetSocketAddress, String> clientData = new HashMap<>();

        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
            System.out.println("Server running on port " + SERVER_PORT);

            byte[] receiveBuffer = new byte[BUFFER_SIZE];

            while (true) {
                // Receive data from a client
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());
                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(receivedData);

                // Update the client's data
                clientData.put(clientAddress, receivedData);

                // Process data (abstracted in this example)
                String processedData = processData(receivedData);
                System.out.println(processedData);

                // Broadcast processed data to all connected clients
                for (InetSocketAddress client : clientData.keySet()) {
                    byte[] sendData = processedData.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getAddress(), client.getPort());
                    serverSocket.send(sendPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simulates processing of client data
    private String processData(String receiveData) {
        // Deserialize JSON into a Map
        Map<String, String> data = this.gson.fromJson(receiveData, Map.class);

        // Retrieve the values
        String token = data.get("token");
        String state = data.get("state");

        // some computation
        switch (state) {
            case "FORWARD":
                this.coords.y += 1;
                break;

            case "BACKWARD":
                this.coords.y -= 1;
                break;

            case "LEFT":
                this.coords.x -= 1;
                break;

            case "RIGHT":
                this.coords.x += 1;
                break;

            default:
                break;
        }

        return this.gson.toJson(this.coords);
    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer();
        server.server();
    }
}