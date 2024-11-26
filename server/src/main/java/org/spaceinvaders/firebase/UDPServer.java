package org.spaceinvaders.firebase;

import java.net.*;
import com.google.gson.Gson;
import org.spaceinvaders.firebase.util.Coordinate;
import org.spaceinvaders.firebase.util.UDPPacket;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class UDPServer
{
    private static final int SERVER_PORT = 9876;
    private static final int BUFFER_SIZE = 10000;

    private final float WORLD_WIDTH = 720;
    private final float WORLD_HEIGHT = 405;

    private final Coordinate coords;
    private final Gson gson;

    public UDPServer()
    {
        this.coords = new Coordinate("spaceship", WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f, 0);
        this.gson = new Gson();
    }

    public void server()
    {
        Map<InetSocketAddress, String> clientData = new HashMap<>();

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

                // Update the client's data
                clientData.put(clientAddress, receivedData);

                // Process data (abstracted in this example)
                String processedData = processData(receivedData);

                // Broadcast processed data to all connected clients
                for (InetSocketAddress client : clientData.keySet()) {
                    byte[] sendData = processedData.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getAddress(), client.getPort());
                    serverSocket.send(sendPacket);
                    System.out.println("Sent data");
                }
            }
        }
        catch (Exception e)
        {
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
        if (state.contains("LEFT")) coords.angle += 1;
        else if (state.contains("RIGHT")) coords.angle -= 1;
        if (state.contains("FORWARD")) moveInDirection(1, coords);
        else if (state.contains("BACKWARD")) moveInDirection(-1, coords);

        return this.gson.toJson(new UDPPacket(this.coords));
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
}