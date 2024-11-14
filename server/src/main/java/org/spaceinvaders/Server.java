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

package org.spaceinvaders;

import com.google.firebase.ErrorCode;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.NetworkNotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server
{
    public static final int BACKLOG_LIMIT = 50;         //The max number of queued incoming connections allowed
    public static final int PORT = 8080;                //The port number of the server

    public static void main(String[] args) throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG_LIMIT);
        server.createContext("/signup", new SignUpHandler());
        server.setExecutor(null);
        server.start();
        LoggerUtil.logInfo("Server started at http://localhost:" + PORT);
    }

    private static class SignUpHandler implements HttpHandler
    {
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

                    String email = json.get("email").getAsString();
                    String password = json.get("password").getAsString();

                    Firebase.getInstance().createUser(email, password);
                    String response = "User Created";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody())
                    {
                        os.write(response.getBytes());
                    }
                }
                catch(JsonSyntaxException e)
                {
                    sendErrorResponse(exchange, 400, "Invalid JSON Format");
                }
                catch(NullPointerException e)
                {
                    sendErrorResponse(exchange, 400, "Invalid email or password");
                }
                catch(NetworkNotFoundException e)
                {
                    sendErrorResponse(exchange, 500, "Cannot connect to the network");
                }
                catch(FirebaseAuthException e)
                {
                    ErrorCode code = e.getErrorCode();

                    if(code.equals(ErrorCode.ALREADY_EXISTS))
                        sendErrorResponse(exchange, 400, "Email already exists");
                    else
                        sendErrorResponse(exchange, 400, "Password is too weak");
                }
                catch (Exception e)
                {
                    sendErrorResponse(exchange, 500, "Failed to sign up user");
                }
            }
            else
            {
                sendErrorResponse(exchange, 500, "Failed to sign up user");
            }
        }
    }

    private static void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException
    {
        exchange.sendResponseHeaders(statusCode, errorMessage.length());
        try (OutputStream os = exchange.getResponseBody())
        {
            os.write(errorMessage.getBytes());
        }
    }
}
