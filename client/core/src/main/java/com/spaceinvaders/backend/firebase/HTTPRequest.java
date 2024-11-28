package com.spaceinvaders.backend.firebase;

import com.spaceinvaders.backend.firebase.utils.HTTPCode;
import com.spaceinvaders.util.LoggerUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * HTTPRequest.java
 * <br>
 * The HTTPRequest class provides methods to send HTTP requests to a server.
 * It supports various HTTP methods (GET, POST, PUT, DELETE) and handles both
 * sending data (e.g., POST/PUT) and receiving server responses.
 * This class also supports sending additional headers with the request, such as
 * Authorization or Content-Type.
 * Key Features:
 * - Supports HTTP methods: GET, POST, PUT, DELETE.
 * - Allows sending JSON payload with POST/PUT requests.
 * - Handles HTTP headers such as Content-Type.
 * - Parses the server response and returns it as a map containing the response code and message.
 * Example usage:
 * ```java
 * try {
 *     HashMap<Integer, String> response = HTTPRequest.sendRequest(
 *         "https://example.com/api",
 *         "{\"key\":\"value\"}",
 *         "POST",
 *         headers);
 *     System.out.println("Response Code: " + response.get(200));
 * } catch (IOException | URISyntaxException e) {
 *     e.printStackTrace();
 * }
 * ```
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public class HTTPRequest {

    /**
     * This method takes care of sending requests to a general HTTP server.
     *
     * @param urlString The URL to which the request is sent.
     * @param payload Data to send with the request (for POST/PUT methods).
     * @param method The HTTP method (e.g., GET, POST, PUT, DELETE).
     * @param headers Additional HTTP headers for the request (e.g., Authorization or Content-Type).
     * @return A map containing the response code and message.
     * @throws IOException If there’s an issue with input/output (e.g., connection failure).
     * @throws URISyntaxException If the provided URL string is not valid.
     */
    public static HashMap<Integer, String> sendRequest(String urlString, String payload, String method, Map<String, String> headers)
        throws IOException, URISyntaxException {

        URL url = (new URI(urlString)).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        // Telling the connection that it's for sending data
        connection.setDoOutput(true);

        // Setting the headers
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        // Sending payload for POST/PUT methods
        if (payload != null && (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT"))) {
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                LoggerUtil.logException("Error: Unable to connect to server: ", e);
                HashMap<Integer, String> hm = new HashMap<>();
                hm.put(HTTPCode.ERROR.getCode(), e.toString());
                return hm;
            }
        }

        return getIntegerStringHashMap(connection);
    }

    /**
     * Retrieves the response from the connection as a HashMap containing response code and message.
     *
     * @param connection The HTTP connection to get the response from.
     * @return A HashMap with response code and message.
     * @throws IOException If there’s an issue reading the response.
     */
    private static HashMap<Integer, String> getIntegerStringHashMap(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        // Choose input stream based on response code
        try (Scanner scanner = new Scanner(responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        HashMap<Integer, String> hm = new HashMap<>();
        hm.put(responseCode, response.toString());
        return hm;
    }
}
