package com.spaceinvaders.backend.firebase.utils;

import com.spaceinvaders.util.LoggerUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

/**
 * HTTPRequest.java
 * <br>
 * This utility class provides methods for sending HTTP requests to a server.
 * It supports various HTTP method POST along with custom headers and optional
 * payload data. It handles HTTP responses and encapsulates the response data
 * in an HttpResponse object.
 * Key features:
 * - Simplifies sending HTTP requests with different methods and headers.
 * - Encapsulates HTTP responses in a dedicated response object for better readability.
 * - Handles exceptions like malformed URLs or IO issues during the connection.
 * Note:
 * Ensure that the provided URL is valid and properly encoded.
 * Handle exceptions appropriately when invoking the `sendRequest` method.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public class HTTPRequest {
    /**
     * Sends an HTTP request to the specified server URL with optional payload and headers.
     *
     * @param urlString             The URL to which the request is sent.
     * @param payload               Data to send with the request (required for POST/PUT methods, optional for others).
     * @param method                The HTTP method to use (e.g., GET, POST, PUT, DELETE).
     * @param headers               Additional HTTP headers for the request (e.g., Authorization, Content-Type).
     * @return  An HttpResponse object containing the HTTP response code and response body.
     * @throws IOException          If there’s an issue with input/output (e.g., connection failure).
     * @throws URISyntaxException   If the provided URL string is not valid.
     */
    public static HttpResponse sendRequest(String urlString, String payload, String method, Map<String, String> headers) throws IOException, URISyntaxException {
        // Convert the string URL to a URI object and then to a URL
        URL url = (new URI(urlString)).toURL();

        // Open the HTTP connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        // Allow output for the connection (needed for POST/PUT payloads)
        connection.setDoOutput(true);

        // Set HTTP headers if provided
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        // Write payload data if present and method is POST/PUT
        if (payload != null && (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT"))) {
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                LoggerUtil.logInfo("Error: Unable to connect to server: " + e);
                return new HttpResponse(HTTPCode.ERROR.getCode(), e.toString());
            }
        }

        // Process the HTTP response
        return getHttpResponse(connection);
    }

    /**
     * Processes the HTTP response and reads the response body based on the response code.
     *
     * @param connection    The HttpURLConnection object representing the connection.
     * @return              An HttpResponse object containing the response code and response body.
     * @throws IOException  If there’s an issue reading the response stream.
     */
    private static HttpResponse getHttpResponse(HttpURLConnection connection) throws IOException {
        // Get the HTTP response code
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        // Choose the appropriate input stream based on the response code
        try (Scanner scanner = new Scanner(responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        // Return the response encapsulated in an HttpResponse object
        return new HttpResponse(responseCode, response.toString());
    }
}
