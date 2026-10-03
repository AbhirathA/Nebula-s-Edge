package org.spaceinvaders.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * GameConfig.java
 * <br>
 * Loads the tunable game constants (world size, radii, masses, speeds) from
 * {@code gameConstants.json} on the classpath. These values used to be served
 * from a Firebase Realtime Database; keeping them in a bundled file lets the
 * server start without any external service.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/28/2024
 */
public final class GameConfig {

    /**
     * Name of the classpath resource holding the constants.
     */
    public static final String RESOURCE = "gameConstants.json";

    private static final JsonObject CONSTANTS = load();

    private GameConfig() {}

    private static JsonObject load() {
        try (InputStream in = GameConfig.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Missing classpath resource " + RESOURCE);
            }
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + RESOURCE, e);
        }
    }

    /**
     * Returns an integer constant.
     * @param key the constant's name, e.g. {@code "GAME_WIDTH"}
     * @return its value
     * @throws IllegalStateException if the constant is not defined
     */
    public static int getInt(String key) {
        JsonElement value = CONSTANTS.get(key);
        if (value == null) {
            throw new IllegalStateException("Constant " + key + " is missing from " + RESOURCE);
        }
        return value.getAsInt();
    }
}
