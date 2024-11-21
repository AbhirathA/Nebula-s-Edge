/**
 * ClientFirebase.java
 * This class provides methods to interact with Firebase's Authentication REST API.
 * It includes functionality to sign in a user with email and password, returning a secure ID token upon success.
 * Key Features:
 * - Sign-in functionality to authenticate users via Firebase.
 * - Parses and extracts the ID token from Firebase's JSON response.
 * Usage Example:
 * ```
 * try
 * {
 *     HttpResponse response = ClientFirebase.signIn("user@example.com", "securePassword");
 *     System.out.println("Authentication successful. Token: " + response.getMessage());
 * }
 * catch (AuthenticationException e)
 * {
 *     System.err.println("Authentication failed: " + e.getMessage());
 * }
 * ```
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */

package com.spaceinvaders.backend.firebase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spaceinvaders.backend.firebase.utils.AuthenticationException;
import com.spaceinvaders.backend.firebase.utils.HTTPCode;
import com.spaceinvaders.backend.firebase.utils.HTTPRequest;
import com.spaceinvaders.backend.firebase.utils.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class ClientFirebase
{
    // Firebase API Key (replace with your own for production use)
    public static final String PUBLIC_FIREBASE_API_KEY = "AIzaSyB5uPRGEMilBLExcfU9w1nKaY0I0Xye7D8";

    // Firebase sign-in endpoint
    public static final String FIREBASE_SIGN_IN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";

    /**
     * Authenticates a user with Firebase using their email and password.
     * Sends a POST request to Firebase's Authentication REST API and retrieves
     * an ID token if the credentials are valid.
     *
     * @param email                     The email address of the user attempting to sign in.
     * @param password                  The password associated with the user's email.
     * @return                          An HttpResponse object containing the success code and the ID token.
     *                                  The ID token can be used to securely access Firebase services.
     * @throws AuthenticationException  If the response indicates authentication failure or any other issue occurs.
     * @throws IllegalStateException    If parsing the JSON response fails.
     */
    public static HttpResponse signIn(String email, String password) throws AuthenticationException, IllegalStateException
    {
        // Constructs the Firebase sign-in URL
        String url = FIREBASE_SIGN_IN_URL + PUBLIC_FIREBASE_API_KEY;

        // Prepares the JSON payload with the user's credentials
        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        // Sets the required headers for the HTTP request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try
        {
            // Sends the HTTP POST request to Firebase
            HttpResponse output = HTTPRequest.sendRequest(url, payload, "POST", headers);

            // Parses the response and extracts the ID token
            return new HttpResponse(HTTPCode.SUCCESS.getCode(), parseIdToken(output.getMessage()));
        }
        catch (Exception e)
        {
            // Handles exceptions and rethrows as an AuthenticationException
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    /**
     * Parses the JSON response from Firebase to extract the ID token.
     *
     * @param response                  The JSON response string containing the ID token.
     * @return                          A String representing the ID token extracted from the response.
     * @throws IllegalStateException    If an error occurs while parsing the JSON response.
     */
    private static String parseIdToken(String response) throws IllegalStateException
    {
        // Parses the JSON response string into a JsonObject
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        // Extracts and returns the ID token
        return jsonObject.get("idToken").getAsString();
    }
}
