package org.spaceinvaders.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.HTTPCode;

/**
 * SignUpHandler.java
 * <br>
 * Handler for processing user sign-up requests.
 * This handler expects a JSON payload containing "email" and "password" to create a new user.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/27/2024
 */
public class SignUpHandler extends BaseHandler {

    /**
     * Processes the incoming HTTP request to create a new user in Firebase.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request and response.
     * @param json     the {@link JsonObject} parsed from the request body, expected to contain "email" and "password".
     * @throws Exception if an error occurs during user creation.
     */
    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws Exception {
        // Extract email and password from the JSON request.
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();

        // Create a new user in Firebase using the provided email and password.
        Firebase.getInstance().createUser(email, password);

        // Send a success response indicating the user was created.
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), "User Created");
    }
}
