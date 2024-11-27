package org.spaceinvaders.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.HTTPCode;

import java.util.Map;

public class GetDataHandler extends BaseHandler {
    @Override
    protected void processRequest(HttpExchange exchange, JsonObject json) throws Exception {
        String idToken = json.get("idToken").getAsString();
        Map<String, Object> userData = Firebase.getInstance().getUserData(idToken);
        String response = new Gson().toJson(userData);
        sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), response);
    }
}
