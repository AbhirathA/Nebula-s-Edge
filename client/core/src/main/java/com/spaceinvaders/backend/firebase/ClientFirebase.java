package com.spaceinvaders.backend.firebase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spaceinvaders.util.AuthenticationException;
import com.spaceinvaders.util.HTTPRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ClientFirebase
{
    public static final String PUBLIC_FIREBASE_API_KEY = "AIzaSyB5uPRGEMilBLExcfU9w1nKaY0I0Xye7D8";
    public static final String FIREBASE_SIGN_IN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    public static final String SERVER_SIGN_UP_URL = "http://localhost:8080/signup";

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
        //connects with the firebase server
        String url = FIREBASE_SIGN_IN_URL + PUBLIC_FIREBASE_API_KEY;

        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try
        {
            return parseIdToken(HTTPRequest.sendRequest(url, payload, "POST", headers));
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

            HTTPRequest.sendRequest(SERVER_SIGN_UP_URL, payload, "POST", headers);
        }
        catch(IOException | URISyntaxException e)
        {
            throw new AuthenticationException("Failed to authenticate the user.");
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
