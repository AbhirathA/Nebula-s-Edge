package com.spaceinvaders.backend.firebase;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    private static final String SERVER_SIGN_UP_URL = "http://localhost:8080/signup";

    private static Map<String, String> makeHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return headers;
    }


}
