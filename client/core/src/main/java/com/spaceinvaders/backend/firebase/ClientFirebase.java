package com.spaceinvaders.backend.firebase;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spaceinvaders.backend.firebase.utils.*;
import com.spaceinvaders.backend.firebase.utils.HTTPRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * ClientFirebase.java
 * <br>
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
public class ClientFirebase {
    // Firebase API Key (replace with your own for production use)
    public static final String PUBLIC_FIREBASE_API_KEY = "AIzaSyB5uPRGEMilBLExcfU9w1nKaY0I0Xye7D8";

    // Firebase sign-in endpoint
    public static final String FIREBASE_SIGN_IN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";

    //Firebase reset password endpoint
    public static final String FIREBASE_RESET_PASSWORD_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=";

    /**
     * Authenticates a user with Firebase using their email and password.
     * Sends a POST request to Firebase's Authentication REST API and retrieves
     * an ID token if the credentials are valid.
     *
     * @param email    The email address of the user attempting to sign in.
     * @param password The password associated with the user's email.
     * @return An HttpResponse object containing the success code and the ID token.
     *         The ID token can be used to securely access Firebase services.
     * @throws AuthenticationException If the response indicates authentication
     *                                 failure or any other issue occurs.
     * @throws IllegalStateException   If parsing the JSON response fails.
     */
    public static HttpResponse signIn(String email, String password)
            throws AuthenticationException, IllegalStateException {
        // Constructs the Firebase sign-in URL
        String url = FIREBASE_SIGN_IN_URL + PUBLIC_FIREBASE_API_KEY;

        // Prepares the JSON payload with the user's credentials
        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email,
                password);

        // Sets the required headers for the HTTP request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            // Sends the HTTP POST request to Firebase
            HttpResponse output = HTTPRequest.sendRequest(url, payload, "POST", headers);

            try {
                handleFirebaseError(output.getMessage());
            } catch (AuthenticationException e) {
                throw new FirebaseAuthenticationException(e.getMessage());
            } catch(Exception ignored) {

            }

            // Parses the response and extracts the ID token
            return new HttpResponse(HTTPCode.SUCCESS.getCode(), parseToken(output.getMessage(), "idToken"));
        } catch(FirebaseAuthenticationException e) {
            throw new AuthenticationException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    /**
     * Parses the JSON response from Firebase to extract the ID token.
     *
     * @param response The JSON response string containing the ID token.
     * @return A String representing the ID token extracted from the response.
     * @throws IllegalStateException If an error occurs while parsing the JSON
     *                               response.
     */
    private static String parseToken(String response, String token) throws IllegalStateException {
        // Parses the JSON response string into a JsonObject
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        // Extracts and returns the ID token
        return jsonObject.get(token).getAsString();
    }

    /**
     * Sends a password reset request to Firebase for the specified email.
     *
     * @param email The email address of the user requesting a password reset.
     * @return The HttpResponse from Firebase indicating the result of the password reset request.
     * @throws AuthenticationException If there is an error during the request or if the response code is not successful.
     */
    public static HttpResponse resetPassword(String email) throws AuthenticationException {

        // Constructing the Firebase reset password URL with the public API key
        String url = FIREBASE_RESET_PASSWORD_URL + PUBLIC_FIREBASE_API_KEY;

        // Payload for the password reset request containing the email
        String payload = String.format("{\"requestType\":\"PASSWORD_RESET\",\"email\":\"%s\"}", email);

        // Headers for the HTTP request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            // Sending the HTTP request to Firebase
            HttpResponse response = HTTPRequest.sendRequest(url, payload, "POST", headers);

            // Checking if the response code indicates success
            if (response.getCode() == HTTPCode.SUCCESS.getCode()) {
                return response;
            } else {
                // Handling any error response from Firebase
                handleFirebaseError(response.getMessage());
            }

            // Default return for unexpected scenarios
            throw new AuthenticationException("Unexpected error occurred during password reset.");
        } catch (Exception e) {
            // Wrapping the exception in an AuthenticationException
            throw new AuthenticationException(e.getMessage());
        }
    }

    /**
     * Handles Firebase error responses by parsing the error message and throwing
     * an appropriate AuthenticationException.
     *
     * @param responseMessage The response body containing the error details.
     * @throws AuthenticationException with a detailed message based on the Firebase
     *                                 error.
     */
    private static void handleFirebaseError(String responseMessage) throws AuthenticationException {
        JsonObject jsonObject = JsonParser.parseString(responseMessage).getAsJsonObject();
        String errorCode = jsonObject.getAsJsonObject("error").get("message").getAsString();

        switch (errorCode) {
            case "EMAIL_NOT_FOUND":
                throw new AuthenticationException("The email address is not registered.");
            case "INVALID_EMAIL":
                throw new AuthenticationException("The email address format is invalid.");
            default:
                throw new AuthenticationException("Firebase error: " + errorCode);
        }
    }

}
