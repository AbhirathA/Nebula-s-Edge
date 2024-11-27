/**
 * GetDataHandler.java
 * Handler for processing requests to retrieve user data from Firebase.
 * This handler expects a JSON payload containing an "idToken" to authenticate the request.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/27/2024
 */

package org.spaceinvaders.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.HTTPCode;

import java.util.Map;

public class GetDataHandler extends BaseHandler {

    /**
     * Processes the incoming HTTP request, retrieves user data from Firebase, and sends the response.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request and response.
     * @param json     the {@link JsonObject} parsed from the request body, expected to contain the "idToken".
     * @throws Exception if an error occurs during request processing or data retrieval.
     */
    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws Exception {
        // Extract the idToken from the JSON request.
        String idToken = json.get("idToken").getAsString();

        // Retrieve user data from Firebase using the provided idToken.
        Map<String, Object> userData = Firebase.getInstance().getUserData(idToken);
        // Convert the user data to a JSON string for the response.
        String response = new Gson().toJson(userData);

        // Send the HTTP response with the user data.
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), response);
    }
}
