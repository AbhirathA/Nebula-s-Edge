package com.spaceinvaders.backend.firebase;

import com.spaceinvaders.backend.firebase.utils.AuthenticationException;
import com.spaceinvaders.backend.firebase.utils.HTTPCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager
{
    public static final String SERVER_URL = "http://localhost:8080/";

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
     * @throws AuthenticationException If the response indicates authentication failure.
     */
    public static String signIn(String email, String password) throws AuthenticationException, IllegalStateException
    {
        String url = SERVER_URL + "signIn";

        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        int returnCode;
        String returnString;
        try
        {
            HashMap<Integer, String> returnValue = HTTPRequest.sendRequest(url, payload, "POST", headers);

            Map.Entry<Integer, String> entry = returnValue.entrySet().iterator().next();
            returnCode = entry.getKey();
            returnString = entry.getValue();

            switch (HTTPCode.fromCode(returnCode)) {
                case SUCCESS:
                    return returnString;

                case INVALID_JSON:
                    throw new AuthenticationException("Error in sending information to server");

                case INVALID_ID_PASS:
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
        }
        catch(Exception e)
        {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }

    public static String signUp(String email, String password, String confirmPassword) throws AuthenticationException
    {
        if(!password.equals(confirmPassword))
        {
            throw new AuthenticationException("Passwords do not match");
        }

        String url = SERVER_URL + "signup";

        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        int returnCode;
        String returnString;

        try
        {
            HashMap<Integer, String> returnValue = HTTPRequest.sendRequest(url, payload, "POST", headers);

            Map.Entry<Integer, String> entry = returnValue.entrySet().iterator().next();
            returnCode = entry.getKey();
            returnString = entry.getValue();

            switch (HTTPCode.fromCode(returnCode)) {
                case SUCCESS:
                    return returnString;

                case INVALID_JSON:
                    throw new AuthenticationException("Error in sending information to server");

                case INVALID_ID_PASS:
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
        }
        catch(IOException | URISyntaxException e)
        {
            throw new AuthenticationException("Failed to authenticate the user.");
        }
    }
}
