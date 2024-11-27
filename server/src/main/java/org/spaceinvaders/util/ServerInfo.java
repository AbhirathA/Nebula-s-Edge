package org.spaceinvaders.util;

import java.net.*;
import java.util.Enumeration;

/**
 * ServerInfo.java
 * <br>
 * The ServerInfo class initializes the server's IP and ports for http and udp services.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/27/2024
 */
public class ServerInfo
{
    /**
     * The IP Address of this server.
     * This is the local IP address of the machine running the server.
     * It is determined dynamically when the class is loaded.
     * If the IP cannot be determined, it defaults to "127.0.0.1" (localhost).
     */
    public static final String IP;

    /**
     * The HTTP Port of this server.
     * This is the port on which the server will listen for HTTP requests.
     */
    public static final int HTTP_PORT = 8080;

    /**
     * The UDP Port of this server.
     * This is the port on which the server will listen for UDP packets.
     */
    public static final int UDP_PORT = 9090;

    static {
        String ipAddress = "127.0.0.1";
        try {
            // getting interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            // iterating over it
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // checking if the networkInterface is a loop-back or is down
                if (networkInterface.isLoopback() || !networkInterface.isUp()) continue;

                // getting the addresses for that interface
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {  // Prefer IPv4
                        ipAddress = addr.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            LoggerUtil.logException(e.getMessage(), e);
        }
        // Assign the determined IP address (or localhost) to the static final variable
        IP = ipAddress;
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This class is meant to be used statically, providing server configuration information.
     */
    private ServerInfo() {}
}
