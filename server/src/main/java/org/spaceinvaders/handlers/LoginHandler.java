package org.spaceinvaders.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.accounts.AccountStore;
import org.spaceinvaders.util.HTTPCode;

/**
 * LoginHandler.java
 * <br>
 * Handler for sign-in requests. Expects a JSON payload containing "email" and
 * "password" and responds with a session token used by {@code /getData}.
 * (Sign-in originally went straight from the client to Firebase Authentication.)
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/27/2024
 */
public class LoginHandler extends BaseHandler {

    /**
     * Checks the credentials and sends back a session token.
     *
     * @param exchange the {@link HttpExchange} object representing the HTTP request and response.
     * @param json     the {@link JsonObject} parsed from the request body, expected to contain "email" and "password".
     * @throws Exception if the credentials are invalid.
     */
    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws Exception {
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();

        String token = AccountStore.getInstance().signIn(email, password);

        JsonObject response = new JsonObject();
        response.addProperty("idToken", token);
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), response.toString());
    }
}
