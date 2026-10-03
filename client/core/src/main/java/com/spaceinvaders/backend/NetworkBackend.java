package com.spaceinvaders.backend;

import com.spaceinvaders.backend.auth.AccountService;
import com.spaceinvaders.backend.auth.HttpAccountService;
import com.spaceinvaders.backend.utils.UDPPacket;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * NetworkBackend.java
 * <br>
 * The desktop backend: accounts and game worlds live on the game server, reached
 * over HTTP and UDP (see {@link com.spaceinvaders.backend.auth.utils.ServerInfo} for the address).
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/24/2024
 */
public class NetworkBackend implements GameBackend {

    private final AccountService accounts = new HttpAccountService();

    @Override
    public AccountService accounts() {
        return accounts;
    }

    @Override
    public GameConnection join(boolean multiplayer, UDPPacket udpPacket, AtomicBoolean hasReceivedPacket) {
        return new UDPClient(udpPacket, hasReceivedPacket, multiplayer);
    }

    @Override
    public boolean supportsScreenshots() {
        return true;
    }
}
