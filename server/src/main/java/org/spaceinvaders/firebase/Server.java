/**
 * Server.java
 * The Server class represents a basic server that handles client requests
 * and manages client connections. This class is responsible for:
 * <ul>
 *      <li>Initializing and starting the server on a specified port.</li>
 *      <li>Accepting client connections and establishing communication channels.</li>
 *      <li>Handling incoming client requests and processing responses.</li>
 * </ul>
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/13/2024
 */

package org.spaceinvaders.firebase;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.firebase.util.DatabaseAccessException;
import org.spaceinvaders.firebase.util.HTTPCode;
import org.spaceinvaders.firebase.util.LoggerUtil;
import org.spaceinvaders.firebase.util.NetworkNotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server
{
    public static final int BACKLOG_LIMIT = 50;         //The max number of queued incoming connections allowed
    public static final int PORT = 8080;                //The port number of the server

    public static void main(String[] args)
    {
        try
        {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG_LIMIT);
            server.createContext("/signup", new SignUpHandler());
            server.setExecutor(null);
            server.start();
            LoggerUtil.logInfo("Server started at http://localhost:" + PORT);
        }
        catch(IOException e)
        {
            LoggerUtil.logException("Cannot create a localhost server on port " + PORT, e);
        }
    }

    //handler class for sign up
    private static class SignUpHandler implements HttpHandler
    {
        /**
         * Handles a user request to sign up
         *
         * @param exchange      the exchange containing the request from the
         *                      client and used to send the response
         * @throws IOException  if any error occurs in communication with firebase
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            if ("POST".equals(exchange.getRequestMethod()))
            {
                try
                {
                    // Parse request body
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    JsonObject json = JsonParser.parseString(requestBody).getAsJsonObject();

                    //get the payload from the request
                    String email = json.get("email").getAsString();
                    String password = json.get("password").getAsString();

                    //create the user and send code 200
                    Firebase.getInstance().createUser(email, password);
                    sendHTTPResponse(exchange, HTTPCode.SUCCESS.getCode(), "User Created");
                }
                catch(JsonSyntaxException e)
                {
                    //JSON Format is invalid
                    sendHTTPResponse(exchange, HTTPCode.INVALID_JSON.getCode(), "Invalid JSON Format");
                }
                catch(NullPointerException e)
                {
                    //User did not send email or password
                    sendHTTPResponse(exchange, HTTPCode.INVALID_ID_PASS.getCode(), "Invalid email or password");
                }
                catch(NetworkNotFoundException e)
                {
                    //The server is not connected to the internet
                    sendHTTPResponse(exchange, HTTPCode.CANNOT_CONNECT.getCode(), "Cannot connect to the network");
                }
                catch(FirebaseAuthException e)
                {
                    //The email is too weak
                    sendHTTPResponse(exchange, HTTPCode.EMAIL_EXISTS.getCode(), "Email already exists");
                }
                catch(IllegalArgumentException e)
                {
                    //The password is too weak
                    sendHTTPResponse(exchange, HTTPCode.WEAK_PASSWORD.getCode(), "Password is too weak");
                }
                catch(DatabaseAccessException e)
                {
                    //User data could not be created
                    sendHTTPResponse(exchange, HTTPCode.DATABASE_ERROR.getCode(), "Could not create user data");
                }
                catch (Exception e)
                {
                    //any other exception which may have occurred
                    sendHTTPResponse(exchange, HTTPCode.SERVER_ERROR.getCode(), "Failed to sign up user");
                }
            }
            else
            {
                sendHTTPResponse(exchange, HTTPCode.SERVER_ERROR.getCode(), "Failed to sign up user");
            }
        }
    }

    /**
     * Sends an error response to the client
     * @param exchange          the HTTPExchange between the server and the client
     * @param statusCode        the status code for the message (ex. 200)
     * @param message           the error message to send
     * @throws IOException      if any error occurs in sending the message
     */
    private static void sendHTTPResponse(HttpExchange exchange, int statusCode, String message) throws IOException
    {
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody())
        {
            os.write(message.getBytes());
        }
        LoggerUtil.logInfo(message);
    }
}
