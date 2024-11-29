package org.spaceinvaders;

import org.spaceinvaders.util.ServerInfo;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * UDPServerManager.java
 * <br>
 * The UDPServer class handles communication between the game server and clients via UDP.
 * It receives state updates from clients, processes game logic, and sends game state updates
 * back to clients at a regular interval. The server operates in two separate threads:
 * one for network communication and one for game logic.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/29/2024
 */

public class UDPServerManager {

    private final UDPServer multiplayerServer = new UDPServer(ServerInfo.UDP_MULTI_PLAYER_PORT);
    private final HashMap<InetSocketAddress, UDPServer> singlePlayerServers = new HashMap<>();
    private int countOfSinglePlayer = 0;

    public UDPServerManager() {}

    /**
     * This validates a multiplayer client.
     * @param address this is the address of that client
     */
    public void addMultiplayerClient(InetSocketAddress address) {
        this.multiplayerServer.addInetSocketAddress(address);
    }

    /**
     * This validates a single player client and creates a new server for it on the next free port
     * @param address this is the address of the client
     * @return returns the port to which you have to connect ot
     */
    public int addSinglePlayerClient(InetSocketAddress address) {
        this.singlePlayerServers.put(address, new UDPServer(ServerInfo.UDP_MULTI_PLAYER_PORT + this.countOfSinglePlayer));
        return ServerInfo.UDP_SINGLE_PLAYER_PORT + this.countOfSinglePlayer++;
    }

    /**
     * Starts up the singlePlayer world
     * @param clientAddress whose singleplayer world u wanna start
     */
    public void startSinglePlayerServer(InetSocketAddress clientAddress) {
        this.singlePlayerServers.get(clientAddress).startThreads();
    }

    /**
     * Starts up the multiplayer world
     */
    public void startMultiplayerServer() {
        this.multiplayerServer.startThreads();
    }
}
