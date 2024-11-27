package org.spaceinvaders.handlers;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.spaceinvaders.firebase.util.DatabaseAccessException;
import org.spaceinvaders.util.HTTPCode;
import org.spaceinvaders.util.LoggerUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
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

    protected abstract void processRequest(HttpExchange exchange, JsonObject json) throws Exception;

    private void handleException(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof NullPointerException) {
            sendHTTPResponse(exchange, HTTPCode.INVALID_INPUT.getCode(), "Missing required data");
        } else if (e instanceof FirebaseAuthException) {
            sendHTTPResponse(exchange, HTTPCode.UNAUTHORIZED.getCode(), "Authentication error");
        } else if (e instanceof DatabaseAccessException) {
            sendHTTPResponse(exchange, HTTPCode.DATABASE_ERROR.getCode(), "Database error");
        } else {
            sendHTTPResponse(exchange, HTTPCode.SERVER_ERROR.getCode(), "Internal server error");
        }
    }

    /**
     * Sends an error response to the client
     *
     * @param exchange   the HTTPExchange between the server and the client
     * @param statusCode the status code for the message (ex. 200)
     * @param message    the error message to send
     * @throws IOException if any error occurs in sending the message
     */
    public void sendHTTPResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
        LoggerUtil.logInfo(message);
    }
}
