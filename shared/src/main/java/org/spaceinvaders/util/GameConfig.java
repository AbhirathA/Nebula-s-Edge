package org.spaceinvaders.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * GameConfig.java
 * <br>
 * Holds the tunable game constants (world size, radii, masses, speeds) from
 * {@code gameConstants.json}. These values used to be served from a Firebase
 * Realtime Database; keeping them in a bundled file lets the game run without
 * any external service. The server reads the file from the classpath; the browser
 * build passes its contents to {@link #load(String)} before starting a world.
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

    private static Map<String, Integer> constants;

    private GameConfig() {}

    /**
     * Uses the given JSON text as the constants. Call before any game world is created.
     * @param json a flat JSON object whose values are integers (other values are ignored)
     */
    public static synchronized void load(String json) {
        constants = parse(json);
    }

    private static synchronized Map<String, Integer> constants() {
        if (constants == null) {
            try (InputStream in = GameConfig.class.getClassLoader().getResourceAsStream(RESOURCE)) {
                if (in == null) {
                    throw new IllegalStateException("Missing classpath resource " + RESOURCE);
                }
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                for (int n; (n = in.read(buffer)) > 0; ) bytes.write(buffer, 0, n);
                constants = parse(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new IllegalStateException("Could not read " + RESOURCE, e);
            }
        }
        return constants;
    }

    // A deliberately tiny reader for {"KEY": 123, ...}; it keeps this class free of
    // reflection-based JSON libraries so it also compiles for the browser
    static Map<String, Integer> parse(String json) {
        Map<String, Integer> values = new HashMap<>();
        int i = 0;
        while ((i = json.indexOf('"', i)) >= 0) {
            int end = json.indexOf('"', i + 1);
            if (end < 0) break;
            String key = json.substring(i + 1, end);
            int colon = end + 1;
            while (colon < json.length() && Character.isWhitespace(json.charAt(colon))) colon++;
            i = end + 1;
            if (colon >= json.length() || json.charAt(colon) != ':') continue;
            int start = colon + 1;
            while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
            int stop = start;
            if (stop < json.length() && json.charAt(stop) == '-') stop++;
            while (stop < json.length() && Character.isDigit(json.charAt(stop))) stop++;
            if (stop > start && !(stop == start + 1 && json.charAt(start) == '-')) {
                values.put(key, Integer.parseInt(json.substring(start, stop)));
            }
            i = Math.max(i, stop);
        }
        return values;
    }

    /**
     * Returns an integer constant.
     * @param key the constant's name, e.g. {@code "GAME_WIDTH"}
     * @return its value
     * @throws IllegalStateException if the constant is not defined
     */
    public static int getInt(String key) {
        Integer value = constants().get(key);
        if (value == null) {
            throw new IllegalStateException("Constant " + key + " is missing from " + RESOURCE);
        }
        return value;
    }
}
