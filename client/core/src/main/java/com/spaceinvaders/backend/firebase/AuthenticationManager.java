/**
 * AuthenticationManager.java
 * This class handles user authentication and session management.
 * It provides methods for validating credentials, generating authentication tokens,
 * and managing user sessions. This can be integrated with services like Firebase, JWT, or a database.
 * Key Features:
 * - Validates user credentials against a data source (e.g., database or external API).
 * - Generates and validates authentication tokens.
 * - Provides support for session management (e.g., login, logout).
 * Usage example:
 * ```
 * AuthenticationManager authManager = new AuthenticationManager();
 * boolean isAuthenticated = authManager.authenticate("user@example.com", "password123");
 * if (isAuthenticated)
 * {
 *     String token = authManager.generateToken("user@example.com");
 *     System.out.println("Authentication successful. Token: " + token);
 * }
 * else
 * {
 *     System.out.println("Authentication failed.");
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
import com.spaceinvaders.backend.firebase.utils.*;
import com.spaceinvaders.backend.firebase.utils.HTTPRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    public static final String SERVER_URL = "http://" + ServerInfo.getIP() + ":" + ServerInfo.getHttpPort() + "/";

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
     * @throws AuthenticationException If the response indicates authentication
     *                                 failure.
     * @throws IllegalStateException   If an error occurs while parsing the JSON
     *                                 file
     */
    public static String signIn(String email, String password) throws AuthenticationException, IllegalStateException {
        int returnCode;
        String returnString;
        try {
            HttpResponse response = ClientFirebase.signIn(email, password);
            returnCode = response.getCode();
            returnString = response.getMessage();

            switch (HTTPCode.fromCode(returnCode)) {
                case SUCCESS:
                    return returnString;

                case INVALID_JSON:
                    throw new AuthenticationException("Error in sending information to server");

                case INVALID_INPUT:
                    throw new AuthenticationException("Invalid Id or password");

                case CANNOT_CONNECT:
                    throw new AuthenticationException("Server cannot connect to network");

                case DATABASE_ERROR:
                    throw new AuthenticationException("Server database error");

                case SERVER_ERROR:
                    throw new AuthenticationException("Server error");

                default:
                    throw new AuthenticationException("Unknown error");

            }
        } catch(AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    /**
     * Registers a new user with the provided email and password.
     * This method validates the input fields, ensures the password and confirm
     * password match,
     * and then performs the signup logic (i.e. storing in Firebase).
     *
     * @param email           The email address of the user to be registered.
     * @param password        The password chosen by the user.
     * @param confirmPassword Confirmation of the password to ensure accuracy.
     * @throws AuthenticationException If the input validation fails (e.g., invalid
     *                                 email, mismatched passwords).
     */
    public static void signUp(String email, String password, String confirmPassword) throws AuthenticationException {
        if (!password.equals(confirmPassword)) {
            throw new AuthenticationException("Passwords do not match");
        }

        String url = SERVER_URL + "signup";

        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email,
                password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        int returnCode;

        try {
            HttpResponse response = HTTPRequest.sendRequest(url, payload, "POST", headers);
            returnCode = response.getCode();

            switch (HTTPCode.fromCode(returnCode)) {
                case SUCCESS:
                    break;

                case INVALID_JSON:
                    throw new AuthenticationException("Error in sending information to server");

                case INVALID_INPUT:
                    throw new AuthenticationException("Invalid id or password");

                case CANNOT_CONNECT:
                    throw new AuthenticationException("Server cannot connect to network");

                case EMAIL_EXISTS:
                    throw new AuthenticationException("Account with id already exists");

                case WEAK_PASSWORD:
                    throw new AuthenticationException("Password is too weak");

                case DATABASE_ERROR:
                    throw new AuthenticationException("Server database error");

                case SERVER_ERROR:
                    throw new AuthenticationException("Server error");

                default:
                    throw new AuthenticationException("Unknown error");
            }
        } catch (IOException | URISyntaxException e) {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    /**
     * Gets the user data from the Firebase
     *
     * @param tokenID   the token id of this user
     * @throws AuthenticationException If the input validation fails (e.g., invalid
     *                                 email, mismatched passwords).
     */
    public static String getUserData(String tokenID) throws AuthenticationException {
        String url = SERVER_URL + "getData";

        String payload = String.format("{\"idToken\":\"%s\"}", tokenID);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        int returnCode;
        String returnString;

        try {
            HttpResponse response = HTTPRequest.sendRequest(url, payload, "POST", headers);
            returnCode = response.getCode();
            returnString = response.getMessage();

            switch (HTTPCode.fromCode(returnCode)) {
                case SUCCESS:
                    JsonObject jsonObject = JsonParser.parseString(returnString).getAsJsonObject();
                    return jsonObject.get("killCount").getAsString();

                case INVALID_JSON:
                    throw new AuthenticationException("Error in sending information to server");

                case INVALID_INPUT:
                    throw new AuthenticationException("Invalid id or password");

                case CANNOT_CONNECT:
                    throw new AuthenticationException("Server cannot connect to network");

                case EMAIL_EXISTS:
                    throw new AuthenticationException("Account with id already exists");

                case WEAK_PASSWORD:
                    throw new AuthenticationException("Password is too weak");

                case DATABASE_ERROR:
                    throw new AuthenticationException("Server database error");

                case SERVER_ERROR:
                    throw new AuthenticationException("Server error");

                default:
                    throw new AuthenticationException("Unknown error");
            }
        } catch (IOException | URISyntaxException e) {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    /**
     * Sends a password reset email to the specified email address.
     *
     * @param email The email address of the user requesting a password reset.
     * @throws AuthenticationException If an error occurs during the password reset
     *                                 process.
     */
    public static void resetPassword(String email) throws AuthenticationException {
        HttpResponse response = ClientFirebase.resetPassword(email);
        int responseCode = response.getCode();

        switch (HTTPCode.fromCode(responseCode)) {
            case SUCCESS:
                break;

            case INVALID_JSON:
                throw new AuthenticationException("Error in sending information to the server.");

            case EMAIL_NOT_FOUND:
                throw new AuthenticationException("The provided email address does not exist.");

            case CANNOT_CONNECT:
                throw new AuthenticationException("Server cannot connect to the network.");

            case DATABASE_ERROR:
                throw new AuthenticationException("Server database error.");

            case SERVER_ERROR:
                throw new AuthenticationException("Internal server error occurred.");

            default:
                throw new AuthenticationException("Unknown error occurred during the password reset process.");
        }
    }

}
