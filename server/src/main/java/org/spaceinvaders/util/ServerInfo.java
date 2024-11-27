package org.spaceinvaders.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerInfo
{
    public static final String IP;
    public static final int HTTP_PORT = 8080;
    public static final int UDP_PORT = 9090;

    static {
        String ipAddress;
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ipAddress = "127.0.0.1"; // Fallback to localhost
        }
        IP = ipAddress;
    }

    private ServerInfo() {}
}
