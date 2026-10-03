package com.spaceinvaders.web;

import com.badlogic.gdx.Gdx;
import com.spaceinvaders.backend.GameBackend;
import com.spaceinvaders.backend.GameConnection;
import com.spaceinvaders.backend.auth.AccountService;
import com.spaceinvaders.backend.auth.utils.AuthenticationException;
import com.spaceinvaders.backend.utils.UDPPacket;
import org.spaceinvaders.util.GameConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LocalBackend.java
 * <br>
 * The browser build's backend. There is no game server behind a static web page,
 * so single player worlds run in the tab ({@link LocalGameConnection}), accounts
 * only last for the session, and multiplayer points players to the desktop version.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 10/03/2026
 */
public class LocalBackend implements GameBackend {

    private final AccountService accounts = new SessionAccounts();
    private boolean configLoaded = false;

    @Override
    public AccountService accounts() {
        return accounts;
    }

    @Override
    public GameConnection join(boolean multiplayer, UDPPacket udpPacket, AtomicBoolean hasReceivedPacket) {
        if (multiplayer) {
            return new UnavailableConnection("Multiplayer needs a game server.\nDownload the desktop version from GitHub to play with friends.");
        }
        if (!WasmPhysicsWorld.isReady()) {
            return new UnavailableConnection("The physics engine is still loading.\nPlease try again in a moment.");
        }
        if (!configLoaded) {
            GameConfig.load(Gdx.files.internal(GameConfig.RESOURCE).readString());
            configLoaded = true;
        }
        return new LocalGameConnection(udpPacket, hasReceivedPacket);
    }

    // A connection that only carries an explanation for the player
    private static final class UnavailableConnection implements GameConnection {
        private final String message;

        UnavailableConnection(String message) {
            this.message = message;
        }

        @Override
        public String getError() {
            return message;
        }

        @Override
        public void send(String state, String token) {}

        @Override
        public void startReceiveThread() {}

        @Override
        public void close() {}
    }

    // Accounts that live as long as the tab, so the login screens still work in the demo
    private static final class SessionAccounts implements AccountService {
        private final Map<String, String> passwords = new HashMap<>();

        @Override
        public String signIn(String email, String password) throws AuthenticationException {
            String expected = passwords.get(email.trim().toLowerCase());
            if (expected == null || !expected.equals(password)) {
                throw new AuthenticationException("Invalid Id or password");
            }
            return "session-" + email.trim().toLowerCase();
        }

        @Override
        public void signUp(String email, String password, String confirmPassword) throws AuthenticationException {
            if (!password.equals(confirmPassword)) throw new AuthenticationException("Passwords do not match");
            if (email.trim().isEmpty()) throw new AuthenticationException("Invalid id or password");
            if (password.length() < 6) throw new AuthenticationException("Password needs 6+ characters");
            if (passwords.containsKey(email.trim().toLowerCase())) throw new AuthenticationException("Account with id already exists");
            passwords.put(email.trim().toLowerCase(), password);
        }

        @Override
        public String getUserData(String token) {
            return "0";
        }

        @Override
        public void resetPassword(String email) throws AuthenticationException {
            throw new AuthenticationException("Not available in the web demo");
        }
    }
}
