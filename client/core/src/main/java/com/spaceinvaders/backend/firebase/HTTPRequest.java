package com.spaceinvaders.backend.firebase;

import com.spaceinvaders.backend.firebase.utils.HTTPCode;

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

public class HTTPRequest
{
    /**
     * This method takes care of sending requests to a general http server.
     *
     * @param urlString: The URL to which the request is sent.
     * @param payload: Data to send with the request (for POST/PUT methods).
     * @param method: The HTTP method (e.g., GET, POST, PUT, DELETE).
     * @param headers: Additional HTTP headers for the request (e.g., Authorization or Content-Type).
     * @throws IOException: If there’s an issue with input/output (e.g., connection failure).
     * @throws URISyntaxException: If the provided URL string is not valid.
     */
    public static HashMap<Integer, String> sendRequest(String urlString, String payload, String method, Map<String, String> headers) throws IOException, URISyntaxException
    {
        URL url = (new URI(urlString)).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        // telling the connection that it's for sending data
        connection.setDoOutput(true);

        // setting the headers
        if (headers != null)
            for (Map.Entry<String, String> header : headers.entrySet())
                connection.setRequestProperty(header.getKey(), header.getValue());


        if (payload != null && (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")))
        {
            try (OutputStream os = connection.getOutputStream())
            {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException e) {
                System.out.println("Error: Unable to connect to server: " + e.toString());
                HashMap<Integer, String> hm = new HashMap<>();
                hm.put(HTTPCode.ERROR.getCode(), e.toString());
                return hm;
            }
        }

        return getIntegerStringHashMap(connection);
    }

    private static HashMap<Integer, String> getIntegerStringHashMap(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        // Choose input stream based on response code
        try (Scanner scanner = new Scanner(responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream()))
        {
            while (scanner.hasNextLine())
                response.append(scanner.nextLine());
        }

        HashMap<Integer, String> hm = new HashMap<>();
        hm.put(responseCode, response.toString());
        return hm;
    }
}
