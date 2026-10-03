package com.spaceinvaders.backend.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spaceinvaders.backend.auth.utils.*;
import com.spaceinvaders.backend.auth.utils.HTTPRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthenticationManager.java
 * <br>
 * This class handles user authentication and session management by talking to
 * the game server's HTTP API. (It originally signed users in through Firebase
 * Authentication; the server now keeps its own accounts.)
 * Key Features:
 * - Signs users up and in, returning a session token.
 * - Fetches the signed-in user's profile.
 * - Performs the handshake that registers the client's UDP socket with a game world.
 * Usage example:
 * ```
 * String token = AuthenticationManager.signIn("pilot", "password123");
 * String killCount = AuthenticationManager.getUserData(token);
 * ```
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/21/2024
 */
public class AuthenticationManager {

    private static Map<String, String> jsonHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private static String credentials(String email, String password) {
        JsonObject payload = new JsonObject();
        payload.addProperty("email", email);
        payload.addProperty("password", password);
        return payload.toString();
    }

    private static HttpResponse post(String endpoint, String payload) throws AuthenticationException {
        try {
            HttpResponse response = HTTPRequest.sendRequest(ServerInfo.getServerUrl() + endpoint, payload, "POST", jsonHeaders());
            if (response.getCode() == HTTPCode.ERROR.getCode()) {
                throw new AuthenticationException("Cannot reach server at " + ServerInfo.getIP());
            }
            return response;
        } catch (IOException | URISyntaxException e) {
            throw new AuthenticationException("Cannot reach server at " + ServerInfo.getIP());
        }
    }

    /**
     * Authenticates a user with the game server using their id and password.
     *
     * @param email    The id (or email address) of the user attempting to sign in.
     * @param password The password associated with the user's id.
     * @return A session token that can be passed to {@link #getUserData(String)}.
     * @throws AuthenticationException If the response indicates authentication
     *                                 failure.
     */
    public static String signIn(String email, String password) throws AuthenticationException {
        HttpResponse response = post("login", credentials(email, password));

        switch (HTTPCode.fromCode(response.getCode())) {
            case SUCCESS:
                try {
                    return JsonParser.parseString(response.getMessage()).getAsJsonObject().get("idToken").getAsString();
                } catch (RuntimeException e) {
                    throw new AuthenticationException("Unexpected response from server");
                }

            case INVALID_JSON:
                throw new AuthenticationException("Error in sending information to server");

            case INVALID_INPUT:
                throw new AuthenticationException("Invalid Id or password");

            case DATABASE_ERROR:
                throw new AuthenticationException("Server database error");

            case SERVER_ERROR:
                throw new AuthenticationException("Server error");

            default:
                throw new AuthenticationException("Unknown error");
        }
    }

    /**
     * Registers a new user with the provided id and password.
     * This method ensures the password and confirm password match,
     * and then asks the server to create the account.
     *
     * @param email           The id (or email address) of the user to be registered.
     * @param password        The password chosen by the user.
     * @param confirmPassword Confirmation of the password to ensure accuracy.
     * @throws AuthenticationException If the input validation fails (e.g., taken
     *                                 id, mismatched passwords).
     */
    public static void signUp(String email, String password, String confirmPassword) throws AuthenticationException {
        if (!password.equals(confirmPassword)) {
            throw new AuthenticationException("Passwords do not match");
        }

        HttpResponse response = post("signup", credentials(email, password));

        switch (HTTPCode.fromCode(response.getCode())) {
            case SUCCESS:
                break;

            case INVALID_JSON:
                throw new AuthenticationException("Error in sending information to server");

            case INVALID_INPUT:
                throw new AuthenticationException("Invalid id or password");

            case EMAIL_EXISTS:
                throw new AuthenticationException("Account with id already exists");

            case WEAK_PASSWORD:
                throw new AuthenticationException("Password needs 6+ characters");

            case DATABASE_ERROR:
                throw new AuthenticationException("Server database error");

            case SERVER_ERROR:
                throw new AuthenticationException("Server error");

            default:
                throw new AuthenticationException("Unknown error");
        }
    }

    /**
     * Gets the kill count of the signed-in user from the server
     *
     * @param tokenID   the session token of this user
     * @return the user's kill count
     * @throws AuthenticationException If the session is not valid.
     */
    public static String getUserData(String tokenID) throws AuthenticationException {
        JsonObject payload = new JsonObject();
        payload.addProperty("idToken", tokenID);
        HttpResponse response = post("getData", payload.toString());

        switch (HTTPCode.fromCode(response.getCode())) {
            case SUCCESS:
                JsonObject jsonObject = JsonParser.parseString(response.getMessage()).getAsJsonObject();
                return jsonObject.get("killCount").getAsString();

            case INVALID_JSON:
                throw new AuthenticationException("Error in sending information to server");

            case UNAUTHORIZED:
                throw new AuthenticationException("Session expired, please log in again");

            case DATABASE_ERROR:
                throw new AuthenticationException("Server database error");

            case SERVER_ERROR:
            default:
                throw new AuthenticationException("Server error");
        }
    }

    /**
     * Asks the server to admit this client's UDP socket to a game world.
     *
     * @param multiplayer whether to join the shared multiplayer world (otherwise a private single player world is created)
     * @param udpPort     the local port of the client's UDP socket
     * @return the server UDP port of the game world to send input to
     * @throws AuthenticationException if the server cannot be reached or refuses the request
     */
    public static int handshake(boolean multiplayer, int udpPort) throws AuthenticationException {
        JsonObject payload = new JsonObject();
        payload.addProperty("type", multiplayer ? "MULTIPLAYER" : "SINGLEPLAYER");
        payload.addProperty("udpPort", udpPort);
        HttpResponse response = post("handshake", payload.toString());

        if (response.getCode() != HTTPCode.SUCCESS.getCode()) {
            throw new AuthenticationException("Server refused to start the game");
        }
        return Integer.parseInt(response.getMessage().trim());
    }

    /**
     * Password reset used to send an email through Firebase. The self-hosted server
     * has no mail service, so this always explains how to proceed instead.
     *
     * @param email The email address of the user requesting a password reset.
     * @throws AuthenticationException always, with a message for the user
     */
    public static void resetPassword(String email) throws AuthenticationException {
        throw new AuthenticationException("Email reset is offline: make a new account");
    }

}
