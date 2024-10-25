package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import java.util.Scanner;

public final class ClientFirebase
{
    private static final String FIREBASE_API_KEY = "AIzaSyB5uPRGEMilBLExcfU9w1nKaY0I0Xye7D8";

    public static String signIn(String email, String password) throws URISyntaxException, IOException, JsonSyntaxException, IllegalStateException
    {
        URI uri = new URI("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        try (OutputStream os = connection.getOutputStream())
        {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            try (Scanner scanner = new Scanner(connection.getInputStream()))
            {
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine())
                {
                    response.append(scanner.nextLine());
                }
                return parseIdToken(response.toString());
            }
        }
        else
        {
            try (Scanner scanner = new Scanner(connection.getErrorStream()))
            {
                StringBuilder errorResponse = new StringBuilder();
                while (scanner.hasNextLine())
                {
                    errorResponse.append(scanner.nextLine());
                }
                throw new IOException("Error signing in: " + errorResponse);
            }
        }
    }

    private static String parseIdToken(String response) throws JsonSyntaxException, IllegalStateException
    {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        return jsonObject.get("idToken").getAsString();
    }
}
