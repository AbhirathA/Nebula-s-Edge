package org.spaceinvaders.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.UDPServerManager;
import org.spaceinvaders.util.HTTPCode;
import org.spaceinvaders.util.ServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * HandshakeHandler.java
 * <br>
 * Handler for processing requests to join a game.
 * This handler expects a JSON payload containing "type" to authenticate the request.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/29/2024
 */

public class HandshakeHandler extends BaseHandler{

    private UDPServerManager serverManager;

    public HandshakeHandler() {
        this.serverManager = new UDPServerManager();
        this.serverManager.startMultiplayerServer();
    }

    /**
     * Handles incoming HTTP requests for handshake, the clients can use this
     * to tell the server they want to connect ot a single player game or
     * multiplayer port.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request and response.
     * @throws IOException if an I/O error occurs during handling.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendHTTPResponse(exchange, HTTPCode.SERVER_ERROR.getCode(), "Invalid HTTP method");
            return;
        }
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(requestBody).getAsJsonObject();
            processRequest(exchange, json);
        } catch (JsonSyntaxException e) {
            sendHTTPResponse(exchange, HTTPCode.INVALID_JSON.getCode(), "Invalid JSON Format");
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws IOException {
        // Extract the type from the JSON request.
        String type = json.get("type").getAsString();

        // connecting to gameEngine
        InetSocketAddress client = exchange.getRemoteAddress();
        int port = -1;
        if (type.equals("MULTIPLAYER")) {
            this.serverManager.addMultiplayerClient(client);
            port = ServerInfo.UDP_MULTI_PLAYER_PORT;
        } else if (type.equals("SINGLEPLAYER")) {
            port = this.serverManager.addSinglePlayerClient(client);
            this.serverManager.startSinglePlayerServer(client);
        } else {
            String error = new Gson().toJson("Invalid type: " + type);
            sendHTTPResponse(exchange, HTTPCode.ERROR.getCode(), error);
            return;
        }

        String response = new Gson().toJson(port);
        // Send the HTTP response with the user data.
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), response);
    }
}
