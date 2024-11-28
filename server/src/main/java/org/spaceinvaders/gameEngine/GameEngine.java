package org.spaceinvaders.gameEngine;

import com.physics.Manager;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.Coordinate;

import java.util.ArrayList;

public class GameEngine {

    public static final int BLACKHOLE_INNER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_INNER_RADIUS").getAsInt();
    public static final int BLACKHOLE_OUTER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_OUTER_RADIUS").getAsInt();
    public static final int BLACKHOLE_MASS = 100;
    public static final int BULLET_RADIUS = Firebase.serverConstants.get("BULLET_RADIUS").getAsInt();
    public static final int BULLET_LIFE = 50;
    public static final int BULLET_SPEED = 0;
    public static final int BULLET_MASS = 1;
    public static final int CAMERA_HEIGHT = Firebase.serverConstants.get("CAMERA_HEIGHT").getAsInt();
    public static final int CAMERA_WIDTH = Firebase.serverConstants.get("CAMERA_WIDTH").getAsInt();
    public static final int ENEMY_RADIUS = Firebase.serverConstants.get("ENEMY_RADIUS").getAsInt();
    public static final int GAME_HEIGHT = Firebase.serverConstants.get("GAME_HEIGHT").getAsInt();
    public static final int GAME_WIDTH = Firebase.serverConstants.get("GAME_WIDTH").getAsInt();
    public static final int BIG_ASTEROID_RADIUS = Firebase.serverConstants.get("BIG_ASTEROID_RADIUS").getAsInt();
    public static final int BIG_ASTEROID_MASS = 150;
    public static final int MEDIUM_ASTEROID_RADIUS = Firebase.serverConstants.get("MEDIUM_ASTEROID_RADIUS").getAsInt();
    public static final int SMALL_ASTEROID_RADIUS = Firebase.serverConstants.get("SMALL_ASTEROID_RADIUS").getAsInt();
    public static final int SPACESHIP_RADIUS = Firebase.serverConstants.get("SPACESHIP_RADIUS").getAsInt();
    public static final int SPACESHIP_MASS = 100;
    public static final int SPACESHIP_HEALTH = 100;
    public static final int PEAK_USER_VEL = 20;
    public static final int DRIFT_USER_VEL = 5;

    private ArrayList<Coordinate> coords;

    private ArrayList<Integer> spaceShipIds;
    private ArrayList<Integer> asteroidIds;
    private ArrayList<Integer> bulletIds;
    private ArrayList<Integer> blackholeIds;

    private Manager gameEngineManager;

    public GameEngine() {
//         TODO: instantiate variables to save for someting ig
        this.coords = new ArrayList<>();

        this.spaceShipIds = new ArrayList<>();
        this.asteroidIds = new ArrayList<>();
        this.bulletIds = new ArrayList<>();
        this.blackholeIds = new ArrayList<>();

        this.gameEngineManager = new Manager(0, 0, 0, GAME_WIDTH, 0, GAME_HEIGHT, 1);
    }

    /**
     * This method should instantiate all non-user objects
     */
    public void instantiateGameEngine() {
//        write code to spawn meteors and asteroids and blackholes
    }

    /**
     * This adds an element. NOTE: for adding a spaceShip please use the function addShip()
     * @param type type of element
     * @param x x*10 coordinate of that element
     * @param y y*10 coordinate of that element
     * @param angle angle*10 of that element
     * @return id of the element
     */
    public int addElement(String type, int x, int y, int angle) {
        int id = 0;
        switch (type) {
            case "SHIP":
                id = this.gameEngineManager.dropUser(x, y, PEAK_USER_VEL, DRIFT_USER_VEL, angle, 10, 10, 25, 144, 0, 0, SPACESHIP_RADIUS, SPACESHIP_RADIUS, SPACESHIP_MASS, SPACESHIP_HEALTH, BULLET_SPEED, BULLET_LIFE);
                this.spaceShipIds.add(id);
                break;

            case "ASTERIOD":
                id = this.gameEngineManager.dropAsteroid(x, y, BIG_ASTEROID_RADIUS, BIG_ASTEROID_RADIUS, BIG_ASTEROID_MASS);
                this.asteroidIds.add(id);
                break;

            case "METEOR":
                id = this.gameEngineManager.dropMeteor(x, y, 0, 0, 0, 0, BIG_ASTEROID_RADIUS, BIG_ASTEROID_RADIUS, BIG_ASTEROID_MASS);
                this.asteroidIds.add(id);
                break;

            case "BLACKHOLE":
                id = this.gameEngineManager.dropBlackHole(x, y, BLACKHOLE_INNER_RADIUS, BLACKHOLE_OUTER_RADIUS, BLACKHOLE_MASS);
                this.blackholeIds.add(id);
                break;
        }

        return id;
    }

    /**
     * This updates the states of any object whose id is known.
     * @param id the id of the object you want to update
     * @param state the new state you want to change it to
     */
    public void updateState(int id, String state) {
        if (state.contains("LEFT")) this.gameEngineManager.left(id);
        else if (state.contains("RIGHT")) this.gameEngineManager.right(id);
        if (state.contains("FORWARD")) this.gameEngineManager.forward(id);
        else if (state.contains("BACKWARD")) this.gameEngineManager.stop(id);
        if (state.contains(("BULLET"))) this.bulletIds.add(this.gameEngineManager.shoot(id, BULLET_RADIUS, BULLET_RADIUS, BULLET_MASS));
    }

    /**
    * This method updates all the objects on the screen by one frame / one physics second.
    */
    public void update() {
        this.gameEngineManager.update();
    }

    /**
     * method to get all coordinate in the private coords arraylist
     */
    public void getAllCoords() {
        this.coords = new ArrayList<>();
        int[][] tempCoords = this.gameEngineManager.display(0, GAME_HEIGHT, GAME_WIDTH, 0);
        
        for (int[] element : tempCoords) {
            this.coords.add(new Coordinate("B", element[0], element[1], element[2], element[3]));
        }
    }

    /**
    * This is a helper function that helps one to divide all the Coordinates to different
    * types.
     *
     * @param type this is the type of coordinates you want
     *
     * @return returns an ArrayList of those coordinates
     */
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

    /**
     * wrapper to create a spaceShip
     * @return id of object
     */
    public int addShip() {
        return this.addElement("SHIP", 0, GAME_HEIGHT, 900);
    }
}
