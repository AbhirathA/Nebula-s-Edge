package org.spaceinvaders.accounts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.spaceinvaders.util.HTTPCode;
import org.spaceinvaders.util.LoggerUtil;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccountStore.java
 * <br>
 * A small self-hosted replacement for the Firebase Authentication + Firestore
 * setup the game originally used. Accounts are kept in a JSON file
 * ({@code data/accounts.json} by default, override with {@code -Dnebula.accounts=path})
 * with salted PBKDF2 password hashes. Signing in returns a random session token that
 * the client sends back to fetch its profile.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/13/2024
 */
public final class AccountStore {

    /**
     * Minimum password length (the same rule Firebase enforced).
     */
    public static final int MIN_PASSWORD_LENGTH = 6;

    private static final int PBKDF2_ITERATIONS = 65_536;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_BYTES = 16;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Path file;
    private final Map<String, Account> accounts;
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    /**
     * One stored account. Field names are what appear in the JSON file.
     */
    private static final class Account {
        String salt;
        String passwordHash;
        int level = 1;
        int killCount = 0;
    }

    private static class Holder {
        private static final AccountStore INSTANCE =
                new AccountStore(Paths.get(System.getProperty("nebula.accounts", "data/accounts.json")));
    }

    /**
     * Returns the process-wide store.
     * @return the account store
     */
    public static AccountStore getInstance() {
        return Holder.INSTANCE;
    }

    private AccountStore(Path file) {
        this.file = file.toAbsolutePath();
        this.accounts = load(this.file);
        LoggerUtil.logInfo("Loaded " + accounts.size() + " account(s) from " + this.file);
    }

    private static Map<String, Account> load(Path file) {
        if (!Files.exists(file)) return new LinkedHashMap<>();
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            Map<String, Account> loaded = GSON.fromJson(reader, new TypeToken<LinkedHashMap<String, Account>>() {}.getType());
            return loaded != null ? loaded : new LinkedHashMap<>();
        } catch (IOException | RuntimeException e) {
            LoggerUtil.logException("Could not read " + file + "; starting with no accounts", e);
            return new LinkedHashMap<>();
        }
    }

    private void save() throws AccountException {
        try {
            Files.createDirectories(file.getParent());
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                GSON.toJson(accounts, writer);
            }
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LoggerUtil.logException("Could not write " + file, e);
            throw new AccountException(HTTPCode.DATABASE_ERROR, "Could not save account data");
        }
    }

    private static String normalise(String id) throws AccountException {
        if (id == null || id.trim().isEmpty()) {
            throw new AccountException(HTTPCode.INVALID_INPUT, "Id must not be empty");
        }
        return id.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Creates a new account.
     * @param id       the user's id (any non-empty name or email, case-insensitive)
     * @param password the password, at least {@value #MIN_PASSWORD_LENGTH} characters
     * @throws AccountException if the id is taken, the password is too short, or saving fails
     */
    public synchronized void createUser(String id, String password) throws AccountException {
        String key = normalise(id);
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new AccountException(HTTPCode.WEAK_PASSWORD, "Password is too weak");
        }
        if (accounts.containsKey(key)) {
            throw new AccountException(HTTPCode.EMAIL_EXISTS, "Account already exists");
        }

        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        Account account = new Account();
        account.salt = Base64.getEncoder().encodeToString(salt);
        account.passwordHash = hash(password, salt);
        accounts.put(key, account);
        save();
        LoggerUtil.logInfo("Created user: " + key);
    }

    /**
     * Checks the credentials and opens a session.
     * @param id       the user's id
     * @param password the user's password
     * @return a session token to pass to {@link #getUserData(String)}
     * @throws AccountException if the credentials are wrong
     */
    public synchronized String signIn(String id, String password) throws AccountException {
        String key = normalise(id);
        Account account = accounts.get(key);
        if (account == null || password == null) {
            throw new AccountException(HTTPCode.INVALID_INPUT, "Invalid id or password");
        }
        byte[] expected = Base64.getDecoder().decode(account.passwordHash);
        byte[] actual = Base64.getDecoder().decode(hash(password, Base64.getDecoder().decode(account.salt)));
        if (!MessageDigest.isEqual(expected, actual)) {
            throw new AccountException(HTTPCode.INVALID_INPUT, "Invalid id or password");
        }

        String token = UUID.randomUUID().toString();
        sessions.put(token, key);
        return token;
    }

    /**
     * Returns the profile of the user that owns the session token.
     * @param token a token returned by {@link #signIn(String, String)}
     * @return the user's public data (email, level, killCount)
     * @throws AccountException if the token is unknown
     */
    public synchronized Map<String, Object> getUserData(String token) throws AccountException {
        String key = token == null ? null : sessions.get(token);
        Account account = key == null ? null : accounts.get(key);
        if (account == null) {
            throw new AccountException(HTTPCode.UNAUTHORIZED, "Session is not valid");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("email", key);
        data.put("level", account.level);
        data.put("killCount", account.killCount);
        return data;
    }

    private static String hash(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH_BITS);
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            spec.clearPassword();
            return Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 is not available on this JVM", e);
        }
    }
}
