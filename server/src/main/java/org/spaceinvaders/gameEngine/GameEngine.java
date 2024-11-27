package org.spaceinvaders.gameEngine;

import com.physics.Manager;
import org.spaceinvaders.util.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;

public class GameEngine {

    private final int WORLD_WIDTH = 720, WORLD_HEIGHT = 405;

    public int count = 0;
    private HashMap<Integer, String> idToState;
    private ArrayList<Coordinate> coords;

    private ArrayList<Integer> spaceShipIds;
    private ArrayList<Integer> asteroidIds;
    private ArrayList<Integer> bulletIds;
    private ArrayList<Integer> blackholeIds;

    private Manager gameEngineManager;

    public GameEngine() {
        // TODO: instantiate variables to save for someting ig
        this.idToState = new HashMap<>();
        this.coords = new ArrayList<>();

        this.spaceShipIds = new ArrayList<>();
        this.asteroidIds = new ArrayList<>();
        this.bulletIds = new ArrayList<>();
        this.blackholeIds = new ArrayList<>();

        this.gameEngineManager = new Manager(0, 0, 0, 7200, 0, -4050, 1);
    }

    // Only adds a spaceShip for now
    public int addElement(String type) {
        this.coords.add(new Coordinate("SHIP", this.count, 0, 0, 90));
        switch (type) {
            case "SHIP":
                this.spaceShipIds.add(count);
                break;

            case "ASTERIOD":
                this.asteroidIds.add(count);
                break;

            case "BULLET":
                this.bulletIds.add(count);
                break;

            case "BLACKHOLE":
                this.blackholeIds.add(count);
                break;
        }

        return count++;
    }

    public void updateState(int id, String state) {
        this.idToState.put(id, state);
    }

    public void update() {
        for (Coordinate coord : this.coords) {
            this.math(this.idToState.get(coord.id), coord);
        }
    }

    public ArrayList<Coordinate> display(String type) {
        ArrayList<Coordinate> retValue = new ArrayList<>();

        switch (type) {
            case "SHIP":
                for (Coordinate coord : this.coords) {
                    if (this.spaceShipIds.contains(coord.id)) retValue.add(coord);
                }
                break;

            case "ASTEROID":
                for (Coordinate coord : this.coords) {
                    if (this.asteroidIds.contains(coord.id)) retValue.add(coord);
                }
                break;

            case "BULLET":
                for (Coordinate coord : this.coords) {
                    if (this.bulletIds.contains(coord.id)) retValue.add(coord);
                }
                break;

            case "BLACKHOLE":
                for (Coordinate coord : this.coords) {
                    if (this.blackholeIds.contains(coord.id)) retValue.add(coord);
                }
                break;
        }

        return retValue;
    }

    private void math(String state, Coordinate coords) {
        if (state.contains("LEFT")) coords.angle += 1;
        else if (state.contains("RIGHT")) coords.angle -= 1;
        if (state.contains("FORWARD")) moveInDirection(1, coords);
        else if (state.contains("BACKWARD")) moveInDirection(-1, coords);
    }

    private void moveInDirection(float speed, Coordinate coords) {
        double angleRad = Math.toRadians(coords.angle) + 1.571f;

        double deltaX = Math.cos(angleRad) * speed;
        double deltaY = Math.sin(angleRad) * speed;

        coords.x += (float) deltaX;
        coords.y += (float) deltaY;

        this.fixCoords(coords);
    }

    private void fixCoords(Coordinate coords) {
        coords.x = Math.min(Math.max(coords.x, 0), this.WORLD_WIDTH - 21) * 10;
        coords.y = Math.min(Math.max(0, coords.y), this.WORLD_HEIGHT - 21) * 10 - 4050;
    }
}
