package com.spaceinvaders.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class HTTPRequest
{
    public static String sendRequest(String urlString, String payload, String method, Map<String, String> headers) throws IOException, URISyntaxException
    {
        URI uri = new URI(urlString);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);

        if (headers != null)
        {
            for (Map.Entry<String, String> header : headers.entrySet())
            {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        if (payload != null && (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")))
        {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream())
            {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }
        }

        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();

        // Choose input stream based on response code
        try (Scanner scanner = new Scanner(
            responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream()))
        {
            while (scanner.hasNextLine())
            {
                response.append(scanner.nextLine());
            }
        }

        // Throw exception for non-2xx responses
        if (responseCode != HttpURLConnection.HTTP_OK)
        {
            throw new IOException("Error in HTTP request: " + response.toString());
        }

        return response.toString();
    }
}
