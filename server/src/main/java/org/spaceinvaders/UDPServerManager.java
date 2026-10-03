package org.spaceinvaders;

import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.ServerInfo;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * UDPServerManager.java
 * <br>
 * Owns the game worlds. There is one shared multiplayer world on
 * {@link ServerInfo#UDP_MULTI_PLAYER_PORT}, and every single-player game gets
 * its own private world on a free port, which shuts itself down when the game ends.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/29/2024
 */

public class UDPServerManager {

    private final UDPServer multiplayerServer = new UDPServer(ServerInfo.UDP_MULTI_PLAYER_PORT, false);

    public UDPServerManager() {}

    /**
     * This validates a multiplayer client.
     * @param address this is the address of that client's UDP socket
     * @return the UDP port of the multiplayer world
     */
    public int addMultiplayerClient(InetSocketAddress address) {
        this.multiplayerServer.addInetSocketAddress(address);
        return this.multiplayerServer.getPort();
    }

    /**
     * Creates and starts a private world for a single player client on a free port.
     * @param address this is the address of the client's UDP socket
     * @return returns the port to which you have to connect to
     * @throws IllegalStateException if no UDP port could be opened
     */
    public int startSinglePlayerServer(InetSocketAddress address) {
        UDPServer server = new UDPServer(0, true);
        server.addInetSocketAddress(address);
        try {
            server.startThreads();
        } catch (SocketException e) {
            throw new IllegalStateException("Could not open a UDP port for a single player world", e);
        }
        LoggerUtil.logInfo("Single player world for " + address + " on port " + server.getPort());
        return server.getPort();
    }

    /**
     * Starts up the multiplayer world
     */
    public void startMultiplayerServer() {
        try {
            this.multiplayerServer.startThreads();
        } catch (SocketException e) {
            throw new IllegalStateException("Could not open UDP port " + ServerInfo.UDP_MULTI_PLAYER_PORT
                    + " for the multiplayer world. Is another server already running?", e);
        }
    }
}
