package org.example;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server 
{
    private static final int PORT = 12345;
    private final List<ClientHandler> clientList = new ArrayList<>(); // List of connected clients

    public Server() 
    {
        // Start the server in a separate thread
        new Thread(this::startServer).start();
    }

    private void startServer() 
    {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) 
        {
            System.out.println("Server started");

            while (true) 
            {
                Socket clientSocket = serverSocket.accept(); // Accept new clients
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                synchronized (clientList) 
                {
                    clientList.add(clientHandler); // Add to list of connected clients
                }
                System.out.println("Client connected. Total clients: " + clientList.size());
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void sendDataToClients(ArrayList<String> dataList) 
    {
        long currentTime = System.currentTimeMillis();
        DataPacket dataPacket = new DataPacket(dataList, currentTime);

        new Thread(() -> 
        {
            synchronized (clientList) 
            {
                // Loop through clients and send data
                for (int i = 0; i < clientList.size(); i++) 
                {
                    ClientHandler clientHandler = clientList.get(i);
                    if (!clientHandler.sendData(dataPacket)) 
                    {
                        // If sending data fails, remove the client
                        clientList.remove(i);
                        i--; // Adjust index after removal
                        System.out.println("Client disconnected. Total clients: " + clientList.size());
                    }
                }
            }
        }).start();
    }

    // Inner class to handle individual clients
    private static class ClientHandler 
    {
        private final Socket socket;
        private final ObjectOutputStream out;

        public ClientHandler(Socket socket) throws IOException 
        {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream()); // Initialize ObjectOutputStream once
        }

        // Method to send data to the client
        public boolean sendData(DataPacket dataPacket) 
        {
            try 
            {
                out.writeObject(dataPacket);
                out.flush();
                return true; // Success
            } 
            catch (IOException e) 
            {
                // If there's an exception, the client is likely disconnected
                try 
                {
                    socket.close(); // Close socket
                } 
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                return false; // Failure
            }
        }
    }
}
