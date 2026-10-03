package com.spaceinvaders.backend;

/**
 * GameConnection.java
 * <br>
 * A link between the gameplay screen and a game world. On desktop the world runs
 * on the game server and this is a UDP socket ({@link UDPClient}); in the browser
 * build the world runs inside the page.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/24/2024
 */
public interface GameConnection {

    /**
     * Returns why the game could not be joined, or null if it was.
     * @return an error message for the player, or null
     */
    String getError();

    /**
     * Sends this frame's input. Called once per rendered frame.
     * @param state the pressed controls, e.g. {@code "FORWARDLEFTBULLET"}
     * @param token the player's session token
     */
    void send(String state, String token);

    /**
     * Starts receiving world snapshots (no-op if already started).
     */
    void startReceiveThread();

    /**
     * Leaves the world and releases its resources.
     */
    void close();
}
