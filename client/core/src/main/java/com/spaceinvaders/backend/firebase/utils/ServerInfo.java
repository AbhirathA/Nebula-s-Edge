/**
 * ServerInfo.java
 * The {@code ServerInfo} class is responsible for retrieving server configuration
 * information (IP address, HTTP port, and UDP port) from a Firebase Realtime Database.
 * It uses Firebase's REST API to fetch data stored under the 'serverInfo' node and
 * provides methods to access the server's configuration.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/27/2024
 */

package com.spaceinvaders.backend.firebase.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerInfo {

    /**
     * The IP address of the server.
     * This value is fetched from the Firebase database and used by the application.
     * Default value is "localhost".
     */
    public static String IP = "localhost";

    /**
     * The HTTP port of the server.
     * This value is fetched from the Firebase database and used by the application.
     * Default value is 8080.
     */
    public static int HTTP_PORT = 8080;

    /**
     * The UDP port of the server.
     * This value is fetched from the Firebase database and used by the application.
     * Default value is 9090.
     */
    public static int UDP_PORT = 9090;

    /**
     * The URL for accessing the Firebase Realtime Database.
     * This URL points to the location where the server configuration data is stored.
     * It is a publicly accessible URL for the database.
     */
    public static final String DATABASE_URL = "https://nebula-s-edge-6e33d-default-rtdb.firebaseio.com/";

    static {
        try {
            // URL for the serverInfo data node (adding .json to indicate the data format)
            URI uri = URI.create(DATABASE_URL + "serverInfo.json");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Get the response from Firebase
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response data from Firebase
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Use Gson to parse the JSON response
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

                // Extract values for ip, httpPort, and udpPort from the JSON response
                IP = jsonResponse.get("ip").getAsString();
                HTTP_PORT = jsonResponse.get("http-port").getAsInt();
                UDP_PORT = jsonResponse.get("udp-port").getAsInt();

            } else {
                throw new Exception("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ServerInfo: " + e.getMessage());
        }
    }

    /**
     * Returns the IP address of the server.
     *
     * @return the IP address of the server as a {@code String}.
     */
    public static String getIP() {
        return IP;
    }

    /**
     * Returns the HTTP port of the server.
     *
     * @return the HTTP port of the server as an {@code int}.
     */
    public static int getHttpPort() {
        return HTTP_PORT;
    }

    /**
     * Returns the UDP port of the server.
     *
     * @return the UDP port of the server as an {@code int}.
     */
    public static int getUdpPort() {
        return UDP_PORT;
    }
}
