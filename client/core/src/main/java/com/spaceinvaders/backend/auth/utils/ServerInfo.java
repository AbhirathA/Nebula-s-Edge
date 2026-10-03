package com.spaceinvaders.backend.auth.utils;

import com.spaceinvaders.util.LoggerUtil;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ServerInfo.java
 * <br>
 * The {@code ServerInfo} class tells the client where the game server is.
 * The server used to publish its address to a Firebase Realtime Database; now the
 * address is configured locally, in order of precedence:
 * <ol>
 *     <li>the {@code nebula.server} system property (Gradle: {@code ./gradlew lwjgl3:run -Pserver=192.168.1.20})</li>
 *     <li>the {@code NEBULA_SERVER} environment variable</li>
 *     <li>{@code 127.0.0.1}, i.e. a server on this machine</li>
 * </ol>
 * The HTTP port can be overridden the same way with {@code nebula.httpPort} / {@code NEBULA_HTTP_PORT}.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 3.0
 * @since 11/27/2024
 */
public class ServerInfo {

    /**
     * Server address used when nothing is configured.
     */
    public static final String DEFAULT_HOST = "127.0.0.1";

    /**
     * HTTP port used when nothing is configured (matches the server's default).
     */
    public static final int DEFAULT_HTTP_PORT = 8080;

    private static final InetAddress ADDRESS = resolve(setting("nebula.server", "NEBULA_SERVER", DEFAULT_HOST));
    private static final int HTTP_PORT = Integer.parseInt(setting("nebula.httpPort", "NEBULA_HTTP_PORT", String.valueOf(DEFAULT_HTTP_PORT)));

    private ServerInfo() {}

    private static String setting(String property, String environmentVariable, String fallback) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) value = System.getenv(environmentVariable);
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private static InetAddress resolve(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            LoggerUtil.logInfo("Game server: " + address.getHostAddress());
            return address;
        } catch (UnknownHostException e) {
            LoggerUtil.logError("Unknown server host '" + host + "', falling back to " + DEFAULT_HOST);
            try {
                return InetAddress.getByName(DEFAULT_HOST);
            } catch (UnknownHostException impossible) {
                throw new IllegalStateException(impossible);
            }
        }
    }

    /**
     * Returns the resolved address of the server. HTTP and UDP both use this exact
     * address so the server sees the same client IP on both protocols.
     *
     * @return the server's address
     */
    public static InetAddress getAddress() {
        return ADDRESS;
    }

    /**
     * Returns the IP address of the server.
     *
     * @return the IP address of the server as a {@code String}.
     */
    public static String getIP() {
        return ADDRESS.getHostAddress();
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
     * Returns the base URL of the server's HTTP API, ending in "/".
     *
     * @return the base URL, e.g. {@code http://127.0.0.1:8080/}
     */
    public static String getServerUrl() {
        String host = ADDRESS instanceof Inet6Address ? "[" + getIP() + "]" : getIP();
        return "http://" + host + ":" + HTTP_PORT + "/";
    }
}
