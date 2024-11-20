package com.spaceinvaders.backend.firebase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spaceinvaders.backend.firebase.utils.AuthenticationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ClientFirebase
{
    public static final String SERVER_URL = "http://localhost:8080/";

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
     * @throws AuthenticationException If the response indicates authentication failure.
     */
    public static String signIn(String email, String password) throws AuthenticationException, IllegalStateException
    {
        String url = SERVER_URL + "signin";

        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try
        {
            HTTPRequest.sendRequest(url, payload, "POST", headers);
        }
        catch(Exception e)
        {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    public static void signUp(String email, String password, String confirmPassword) throws AuthenticationException
    {
        if(!password.equals(confirmPassword))
        {
            throw new AuthenticationException("Passwords do not match");
        }

        try
        {
            String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            HTTPRequest.sendRequest(SERVER_URL + "signup", payload, "POST", headers);
        }
        catch(IOException | URISyntaxException e)
        {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }
}
