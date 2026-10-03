package org.spaceinvaders.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.UDPServerManager;
import org.spaceinvaders.util.HTTPCode;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * HandshakeHandler.java
 * <br>
 * Handler for processing requests to join a game.
 * This handler expects a JSON payload containing "type" ("SINGLEPLAYER" or
 * "MULTIPLAYER") and "udpPort", the local port of the client's UDP socket.
 * The server only accepts UDP packets from (client IP, udpPort) pairs that
 * completed this handshake, and replies with the UDP port of the game world
 * the client should talk to.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/29/2024
 */

public class HandshakeHandler extends BaseHandler{

    private final UDPServerManager serverManager;

    /**
     * Creates the handler and starts the shared multiplayer world.
     */
    public HandshakeHandler() {
        this.serverManager = new UDPServerManager();
        this.serverManager.startMultiplayerServer();
    }

    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws IOException {
        // Extract the type from the JSON request.
        String type = json.get("type").getAsString();
        int udpPort = json.get("udpPort").getAsInt();

        // The UDP packets come from the same host as this HTTP request, but from the client's UDP socket
        InetSocketAddress client = new InetSocketAddress(exchange.getRemoteAddress().getAddress(), udpPort);
        int port;
        if (type.equals("MULTIPLAYER")) {
            port = this.serverManager.addMultiplayerClient(client);
        } else if (type.equals("SINGLEPLAYER")) {
            port = this.serverManager.startSinglePlayerServer(client);
        } else {
            String error = new Gson().toJson("Invalid type: " + type);
            sendHTTPResponse(exchange, HTTPCode.ERROR.getCode(), error);
            return;
        }

        // Send the HTTP response with the UDP port of the game world.
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), new Gson().toJson(port));
    }
}
