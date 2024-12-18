package com.spaceinvaders.backend.firebase.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spaceinvaders.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * ServerInfo.java
 * <br>
 * The {@code ServerInfo} class is responsible for retrieving server configuration
 * and constants (serverInfo, clientConstants) from a Firebase Realtime Database.
 * It uses Firebase's REST API to fetch and parse data dynamically for better scalability.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/27/2024
 */
public class ServerInfo {

    /**
     * The default Firebase Realtime Database URL.
     * Update this URL to point to the correct database location.
     */
    private static final String DATABASE_URL = "https://nebula-s-edge-6e33d-default-rtdb.firebaseio.com/";

    private static final Gson GSON = new Gson();
    private static final JsonObject SERVER_INFO = fetchServerInfo();
    private static final JsonObject CLIENT_CONSTANTS = fetchClientConstants();

    /**
     * Fetches JSON data from the Firebase database for the given node.
     *
     * @param node The node path to fetch data from, e.g., "serverInfo" or "clientConstants".
     * @return A {@code JsonObject} representing the fetched data.
     * @throws Exception If the data fetch fails.
     */
    private static JsonObject fetchData(String node) throws Exception {
        URI uri = URI.create(DATABASE_URL + node + ".json");
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return GSON.fromJson(response.toString(), JsonObject.class);
        } else {
            throw new Exception("GET request failed. Response Code: " + responseCode);
        }
    }

    /**
     * Retrieves server information (IP, HTTP port, UDP port).
     *
     * @return A {@code JsonObject} containing server info.
     */
    private static JsonObject fetchServerInfo() {
        try {
            return fetchData("serverInfo");
        } catch (Exception e) {
            LoggerUtil.logError("Error fetching serverInfo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves client constants (WORLD_WIDTH, WORLD_HEIGHT, etc.).
     *
     * @return A {@code JsonObject} containing client constants.
     */
    private static JsonObject fetchClientConstants() {
        try {
            return fetchData("clientConstants");
        } catch (Exception e) {
            LoggerUtil.logError("Error fetching clientConstants: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns the IP address of the server.
     *
     * @return the IP address of the server as a {@code String}.
     */
    public static String getIP() {
        if (SERVER_INFO != null)
        {
            return SERVER_INFO.get("ip").getAsString();
        }
        LoggerUtil.logInfo("Could not find IP");
        System.exit(0);
        return null;
    }

    /**
     * Returns the HTTP port of the server.
     *
     * @return the HTTP port of the server as an {@code int}.
     */
    public static int getHttpPort() {
        if (SERVER_INFO != null)
        {
            return SERVER_INFO.get("http-port").getAsInt();
        }
        LoggerUtil.logInfo("Could not find http port");
        System.exit(0);
        return 0;
    }

    /**
     * Returns the UDP port of the server.
     *
     * @return the UDP port of the server as an {@code int}.
     */
    public static int getUdpPortSinglePlayer() {
        if (SERVER_INFO != null)
        {
            return SERVER_INFO.get("udp-single-port").getAsInt();
        }
        LoggerUtil.logInfo("Could not find udp port");
        System.exit(0);
        return 0;
    }

    /**
     * Returns the UDP port of the server.
     *
     * @return the UDP port of the server as an {@code int}.
     */
    public static int getUdpPortMultiPlayer() {
        if (SERVER_INFO != null)
        {
            return SERVER_INFO.get("udp-multi-port").getAsInt();
        }
        LoggerUtil.logInfo("Could not find udp port");
        System.exit(0);
        return 0;
    }

    /**
     * Returns the client constants
     *
     * @return  the client constants
     */
    public static JsonObject getClientConstants()
    {
        return CLIENT_CONSTANTS;
    }
}
