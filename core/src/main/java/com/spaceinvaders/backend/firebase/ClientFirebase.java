package com.spaceinvaders.backend.firebase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientFirebase
{
    public static final String PUBLIC_FIREBASE_API_KEY = "AIzaSyB5uPRGEMilBLExcfU9w1nKaY0I0Xye7D8";

    /**
     * Authenticates a user with Firebase using their email and password.
     * Sends a POST request to Firebase's Authentication REST API and retrieves
     * an ID token if the credentials are valid.
     *
     * @param email    The email address of the user attempting to sign in.
     * @param password The password associated with the user's email.
     * @return A String representing the ID token returned by Firebase upon
     *         successful authentication. This token can be used to securely
     *         access Firebase services.
     * @throws URISyntaxException      If the URI for Firebase's REST API is incorrectly formatted.
     * @throws IOException             If an error occurs during the connection.
     * @throws AuthenticationException If the response indicates authentication failure.
     * @throws IllegalStateException   If an error occurs while parsing the JSON response.
     */
    public static String signIn(String email, String password) throws URISyntaxException, IOException, AuthenticationException, IllegalStateException
    {
        //connects with the firebase server
        URI uri = new URI("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + PUBLIC_FIREBASE_API_KEY);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Prepares the JSON payload with the user's email and password.
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        // Sends the JSON payload to the Firebase server.
        try (OutputStream os = connection.getOutputStream())
        {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        // Reads and processes the response.
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // If authentication is successful, reads the ID token from the response.
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
            // If authentication fails, reads the error response.
            try (Scanner scanner = new Scanner(connection.getErrorStream()))
            {
                StringBuilder errorResponse = new StringBuilder();
                while (scanner.hasNextLine())
                {
                    errorResponse.append(scanner.nextLine());
                }
                throw new AuthenticationException("Error signing in: " + errorResponse);
            }
        }
    }

    /**
     * Parses the JSON response from Firebase to extract the ID token.
     *
     * @param response The JSON response string containing the ID token.
     * @return A String representing the ID token.
     * @throws IllegalStateException If an error occurs while parsing the JSON response.
     */
    private static String parseIdToken(String response) throws IllegalStateException
    {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        return jsonObject.get("idToken").getAsString();
    }
}
