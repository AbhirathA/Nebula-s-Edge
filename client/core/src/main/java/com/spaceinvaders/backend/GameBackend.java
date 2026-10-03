package com.spaceinvaders.backend;

import com.spaceinvaders.backend.auth.AccountService;
import com.spaceinvaders.backend.utils.UDPPacket;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GameBackend.java
 * <br>
 * Everything the game needs from outside the UI: accounts and game worlds. The
 * desktop launcher passes {@link NetworkBackend}, which talks to the game server;
 * the browser launcher passes a backend that runs single player worlds locally.
 * Keeping the screens behind this interface is what lets the same UI code compile
 * for the browser, where sockets and HTTP clients don't exist.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/24/2024
 */
public interface GameBackend {

    /**
     * Returns the service used by the login, signup and reset screens.
     * @return the account service
     */
    AccountService accounts();

    /**
     * Joins a game world.
     * @param multiplayer       whether to join the shared world instead of a private single player one
     * @param udpPacket         receives each new world snapshot
     * @param hasReceivedPacket set once the first snapshot arrives
     * @return the connection; check {@link GameConnection#getError()} before playing
     */
    GameConnection join(boolean multiplayer, UDPPacket udpPacket, AtomicBoolean hasReceivedPacket);

    /**
     * Whether F12 screenshots can be written to disk on this platform.
     * @return true on desktop
     */
    default boolean supportsScreenshots() {
        return false;
    }
}
