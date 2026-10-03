package com.spaceinvaders.web;

import com.badlogic.gdx.utils.TimeUtils;
import com.spaceinvaders.backend.GameConnection;
import org.spaceinvaders.gameEngine.GameEngine;
import org.spaceinvaders.util.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LocalGameConnection.java
 * <br>
 * A single player world running inside the browser tab. It does what the server's
 * {@code UDPServer} game thread does — the same {@link GameEngine}, level and 90 Hz
 * tick — but instead of exchanging UDP packets it hands each snapshot straight to
 * the gameplay screen. The world only advances while the screen is sending input,
 * so pausing the game pauses the world.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 10/03/2026
 */
public class LocalGameConnection implements GameConnection {
    private static final int TICKS_PER_SECOND = 90;
    private static final float TICK = 1f / TICKS_PER_SECOND;
    /** Longest gap simulated after a stall (e.g. a backgrounded tab), in seconds */
    private static final float MAX_CATCH_UP = 0.25f;

    private final com.spaceinvaders.backend.utils.UDPPacket screenPacket;
    private final AtomicBoolean hasReceivedPacket;

    private GameEngine engine;
    private int shipId = -1;
    private boolean finished = false;
    private boolean pendingShot = false;
    private float accumulator = 0;
    private long lastNanos;

    public LocalGameConnection(com.spaceinvaders.backend.utils.UDPPacket screenPacket, AtomicBoolean hasReceivedPacket) {
        this.screenPacket = screenPacket;
        this.hasReceivedPacket = hasReceivedPacket;
    }

    @Override
    public String getError() {
        return null;
    }

    @Override
    public void startReceiveThread() {
        // Snapshots are produced synchronously in send()
    }

    @Override
    public void send(String state, String token) {
        if (finished) return;
        long now = TimeUtils.nanoTime();
        if (engine == null) {
            engine = new GameEngine(WasmPhysicsWorld::new);
            engine.instantiateGameEngineObjects();
            shipId = engine.addShip();
            lastNanos = now;
        }

        accumulator = Math.min(accumulator + (now - lastNanos) / 1e9f, MAX_CATCH_UP);
        lastNanos = now;

        // A shot is applied on one tick only, like the server does
        pendingShot |= state.contains("BULLET");
        String movement = state.replace("BULLET", "");

        int ticks = 0;
        while (accumulator >= TICK) {
            engine.updateState(shipId, pendingShot ? movement + "BULLET" : movement);
            pendingShot = false;
            engine.update();
            accumulator -= TICK;
            ticks++;
        }
        if (ticks > 0) publishSnapshot();
    }

    private void publishSnapshot() {
        engine.getAllCoords();
        com.spaceinvaders.backend.utils.UDPPacket packet = new com.spaceinvaders.backend.utils.UDPPacket();
        packet.id = shipId;
        packet.spaceShips = convert(engine.display("SHIP"));
        packet.enemies = convert(engine.display("ENEMY"));
        packet.asteroids = convert(engine.display("ASTEROID"));
        packet.asteroids.addAll(convert(engine.display("METEOR")));
        packet.bullets = convert(engine.display("BULLET"));
        packet.blackholes = convert(engine.display("BLACKHOLES"));
        packet.powerUpH = convert(engine.display("POWERUPH"));
        packet.powerUpP = convert(engine.display("POWERUPP"));
        packet.powerUpB = convert(engine.display("POWERUPB"));

        synchronized (screenPacket) {
            screenPacket.update(packet);
        }
        hasReceivedPacket.set(true);

        boolean alive = false;
        for (com.spaceinvaders.backend.utils.Coordinate ship : packet.spaceShips) {
            if (ship.id == shipId) alive = true;
        }
        // The screen notices the missing ship and shows game over; this world is done
        if (!alive) close();
    }

    // The engine reports server-side Coordinates; the screens draw the client's own class
    private static ArrayList<com.spaceinvaders.backend.utils.Coordinate> convert(List<Coordinate> coordinates) {
        ArrayList<com.spaceinvaders.backend.utils.Coordinate> out = new ArrayList<>(coordinates.size());
        for (Coordinate c : coordinates) {
            out.add(new com.spaceinvaders.backend.utils.Coordinate(c.type, c.id, c.x, c.y, c.angle, c.point, c.health));
        }
        return out;
    }

    @Override
    public void close() {
        finished = true;
        if (engine != null) {
            engine.dispose();
            engine = null;
        }
    }
}
