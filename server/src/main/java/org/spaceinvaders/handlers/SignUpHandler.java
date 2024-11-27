package org.spaceinvaders.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.HTTPCode;

public class SignUpHandler extends BaseHandler {
    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws Exception {
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();
        Firebase.getInstance().createUser(email, password);
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), "User Created");
    }
}
