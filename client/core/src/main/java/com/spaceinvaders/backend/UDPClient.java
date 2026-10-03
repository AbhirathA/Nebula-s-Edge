package com.spaceinvaders.backend;

import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.spaceinvaders.backend.auth.AuthenticationManager;
import com.spaceinvaders.backend.auth.utils.AuthenticationException;
import com.spaceinvaders.backend.auth.utils.ServerInfo;
import com.spaceinvaders.backend.utils.UDPPacket;
import com.spaceinvaders.util.LoggerUtil;

/**
 * UDPClient.java
 * <br>
 * Streams the player's input to the server and receives world snapshots back.
 * The socket is bound to a free local port, which is registered with the server
 * through the HTTP handshake before any UDP traffic is sent.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/24/2024
 */
public class UDPClient {
    private static final int BUFFER_SIZE = 65507; // largest possible UDP payload
    private static final int RECEIVE_TIMEOUT_MS = 500;

    private DatagramSocket clientSocket = null;
    private final InetAddress serverAddress = ServerInfo.getAddress();
    private int serverPort = -1;
    private String error = null;
    private final Gson gson = new Gson();

    public final UDPPacket udpPacket;
    private Thread receiveThread;
    private volatile boolean running = false;

    private final AtomicBoolean hasReceivedPacket;

    /**
     * Creates the client and joins a game world on the server.
     * @param udpPacket         the packet object that receives each new world snapshot
     * @param hasReceivedPacket set once the first snapshot arrives
     * @param multiplayer       whether to join the shared world instead of a private single player one
     */
    public UDPClient(UDPPacket udpPacket, AtomicBoolean hasReceivedPacket, boolean multiplayer) {
        this.udpPacket = udpPacket;
        this.hasReceivedPacket = hasReceivedPacket;
        try {
            this.clientSocket = new DatagramSocket();
            this.clientSocket.setSoTimeout(RECEIVE_TIMEOUT_MS);
            this.serverPort = AuthenticationManager.handshake(multiplayer, this.clientSocket.getLocalPort());
            LoggerUtil.logInfo("Joined " + (multiplayer ? "multiplayer" : "single player") + " world on UDP port " + this.serverPort);
        } catch (SocketException e) {
            this.error = "Could not open a network socket";
            LoggerUtil.logError(this.error + ": " + e.getMessage());
        } catch (AuthenticationException e) {
            this.error = "Cannot reach the game server at " + ServerInfo.getIP();
            LoggerUtil.logError(this.error + ": " + e.getMessage());
        }
    }

    /**
     * Returns why the client could not join a game, or null if it joined.
     * @return an error message for the player, or null
     */
    public String getError() {
        return this.error;
    }

    public void send(String state, String token) {
        if (this.error != null || this.clientSocket.isClosed()) return;
        try {
            byte[] sendData = generateData(state, token).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.serverAddress, this.serverPort);
            this.clientSocket.send(sendPacket);
        } catch (Exception e) {
            LoggerUtil.logError("Could not send input: " + e.getMessage());
        }
    }

    /**
     * Starts the thread that receives world snapshots (no-op if it is already running).
     */
    public void startReceiveThread() {
        if (this.error != null || this.running) return;
        this.running = true;
        this.receiveThread = new Thread(new UDPReceive(), "udp-receive");
        this.receiveThread.setDaemon(true);
        this.receiveThread.start();
    }

    /**
     * Stops receiving and releases the socket.
     */
    public void close() {
        this.running = false;
        if (this.clientSocket != null) this.clientSocket.close();
    }

    private class UDPReceive implements Runnable {

        @Override
        public void run() {
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            while (UDPClient.this.running) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    UDPClient.this.clientSocket.receive(receivePacket);
                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    UDPPacket received = UDPClient.this.gson.fromJson(receivedData, UDPPacket.class);

                    synchronized (UDPClient.this.udpPacket) {
                        UDPClient.this.udpPacket.update(received);
                    }
                    UDPClient.this.hasReceivedPacket.set(true);

                } catch (SocketTimeoutException se) {
                    // No snapshot recently (e.g. paused); check whether we should keep running
                } catch (JsonSyntaxException e) {
                    LoggerUtil.logError("Ignoring malformed snapshot: " + e.getMessage());
                } catch (Exception e) {
                    if (UDPClient.this.running) {
                        LoggerUtil.logError("UDP receive failed: " + e.getMessage());
                    }
                }
            }
        }
    }

    // Builds the JSON input message sent every frame
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
