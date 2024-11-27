package org.spaceinvaders.util;

import java.net.*;
import java.util.Enumeration;

public class GetIPAddress {
    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {  // Prefer IPv4
                        System.out.println("IP Address: " + addr.getHostAddress());
                        return;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}

