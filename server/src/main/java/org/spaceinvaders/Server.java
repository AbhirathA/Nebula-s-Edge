package org.spaceinvaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server
{
    public static final int BACKLOG_LIMIT = 50;
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG_LIMIT);
        server.createContext("/signup", new SignUpHandler());
        server.setExecutor(null);
        server.start();
        //@TODO: Convert to logging
        System.out.println("Server started at http://localhost:8080");
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
                catch (Exception e)
                {
                    String errorMessage = "Failed to sign up user";
                    exchange.sendResponseHeaders(401, errorMessage.length());
                    try (OutputStream os = exchange.getResponseBody())
                    {
                        os.write(errorMessage.getBytes());
                    }
                }
            }
            else
            {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        }
    }
}
