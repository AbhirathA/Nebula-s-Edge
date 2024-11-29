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

/**
 * BaseHandler.java
 * <br>
 * Abstract base class for handling HTTP requests. Provides common functionality for handling
 * POST requests, parsing JSON input, and handling exceptions.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/27/2024
 */
public abstract class BaseHandler implements HttpHandler {
    /**
     * Handles incoming HTTP requests. Only accepts POST requests and parses the request body as JSON.
     * Delegates processing to the {@link #processRequest(HttpExchange, JsonObject)} method.
     * Handles common exceptions and sends appropriate HTTP responses.
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

    /**
     * Processes the HTTP request with the parsed JSON body. This method must be implemented by
     * subclasses to provide specific request handling logic.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request and response.
     * @param json     the {@link JsonObject} parsed from the request body.
     * @throws Exception if an error occurs during request processing.
     */
    protected abstract void processRequest(HttpExchange exchange, JsonObject json) throws Exception;

    /**
     * Handles exceptions that occur during request processing and sends appropriate HTTP responses.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request and response.
     * @param e        the exception that occurred.
     * @throws IOException if an I/O error occurs while sending the response.
     */
    void handleException(HttpExchange exchange, Exception e) throws IOException {
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
     * Sends an HTTP response to the client.
     *
     * @param exchange   the {@link HttpExchange} object representing the HTTP request and response.
     * @param statusCode the HTTP status code to send.
     * @param message    the response message to send.
     * @throws IOException if an I/O error occurs while sending the response.
     */
    public void sendHTTPResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
        LoggerUtil.logInfo(message);
    }
}
