package org.spaceinvaders.gameEngine;

import com.physics.Manager;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.Coordinate;

import java.util.ArrayList;

/**
 * GameEngine.java
 * <br>
 * The {@code GameEngine} class serves as the core engine for managing the game world
 * in a 2D space simulation. It handles the initialization, creation, and management
 * of game objects such as spaceships, asteroids, black holes, and bullets. Additionally,
 * it integrates with the physics engine and utilizes configuration constants from a
 * Firebase server.
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Dynamic initialization of game constants from Firebase.</li>
 *   <li>Object creation and management for spaceships, asteroids, black holes, and bullets.</li>
 *   <li>Physics-based state updates and game world simulation.</li>
 *   <li>Methods for retrieving and filtering object coordinates based on their type.</li>
 * </ul>
 *
 * <p>This class interacts closely with:</p>
 * <ul>
 *   <li>{@code Manager} - A physics engine for handling object dynamics.</li>
 *   <li>{@code Firebase} - A configuration source for game constants.</li>
 *   <li>{@code Coordinate} - A utility class for representing object positions and properties.</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <ol>
 *   <li>Initialize the {@code GameEngine} using the constructor.</li>
 *   <li>Use {@code addElement()} or {@code addShip()} to create objects in the game world.</li>
 *   <li>Call {@code update()} periodically to progress the game simulation.</li>
 *   <li>Retrieve object states using {@code display()} or {@code getAllCoords()}.</li>
 * </ol>
 *
 * <p>Note: Ensure that Firebase server constants are properly configured and accessible
 * before initializing the engine.</p>
 *
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.2
 * @since 11/28/2024
 */
public class GameEngine {

    /** Inner radius of the black hole, divided by 10 for scaling purposes. */
    public static final int BLACKHOLE_INNER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_INNER_RADIUS").getAsInt() / 10;

    /** Outer radius of the black hole, divided by 10 for scaling purposes. */
    public static final int BLACKHOLE_OUTER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_OUTER_RADIUS").getAsInt() / 10;

    /** Mass of the black hole. */
    public static final int BLACKHOLE_MASS = Firebase.serverConstants.get("BLACKHOLE_MASS").getAsInt();

    /** Height of the camera viewport. */
    public static final int CAMERA_HEIGHT = Firebase.serverConstants.get("CAMERA_HEIGHT").getAsInt();

    /** Width of the camera viewport. */
    public static final int CAMERA_WIDTH = Firebase.serverConstants.get("CAMERA_WIDTH").getAsInt();

    /** Radius of enemy ships, divided by 10 for scaling purposes. */
    public static final int ENEMY_RADIUS = Firebase.serverConstants.get("ENEMY_RADIUS").getAsInt() / 10;

    /** Height of the game world. */
    public static final int GAME_HEIGHT = Firebase.serverConstants.get("GAME_HEIGHT").getAsInt();

    /** Width of the game world. */
    public static final int GAME_WIDTH = Firebase.serverConstants.get("GAME_WIDTH").getAsInt();

    /** Radius of big asteroids, divided by 10 for scaling purposes. */
    public static final int BIG_ASTEROID_RADIUS = Firebase.serverConstants.get("BIG_ASTEROID_RADIUS").getAsInt() / 10;

    /** Mass of big asteroids. */
    public static final int BIG_ASTEROID_MASS = Firebase.serverConstants.get("BIG_ASTEROID_MASS").getAsInt();

    /** Radius of medium asteroids, divided by 10 for scaling purposes. */
    public static final int MEDIUM_ASTEROID_RADIUS = Firebase.serverConstants.get("MEDIUM_ASTEROID_RADIUS").getAsInt() / 10;

    /** Radius of small asteroids, divided by 10 for scaling purposes. */
    public static final int SMALL_ASTEROID_RADIUS = Firebase.serverConstants.get("SMALL_ASTEROID_RADIUS").getAsInt() / 10;

    /** Radius of spaceships, divided by 10 for scaling purposes. */
    public static final int SPACESHIP_RADIUS = Firebase.serverConstants.get("SPACESHIP_RADIUS").getAsInt() / 10;

    /** Mass of spaceships. */
    public static final int SPACESHIP_MASS = Firebase.serverConstants.get("SPACESHIP_MASS").getAsInt();

    /** Health of spaceships. */
    public static final int SPACESHIP_HEALTH = Firebase.serverConstants.get("SPACESHIP_HEALTH").getAsInt();

    /** Peak velocity of user-controlled spaceships. */
    public static final int PEAK_USER_VEL = Firebase.serverConstants.get("PEAK_USER_VEL").getAsInt();

    /** Drift velocity of user-controlled spaceships. */
    public static final int DRIFT_USER_VEL = Firebase.serverConstants.get("DRIFT_USER_VEL").getAsInt();

    /** Radius of bullets, divided by 10 for scaling purposes. */
    public static final int BULLET_RADIUS = Firebase.serverConstants.get("BULLET_RADIUS").getAsInt() / 10;

    /** Lifespan of bullets in frames or ticks. */
    public static final int BULLET_LIFE = Firebase.serverConstants.get("BULLET_LIFE").getAsInt();

    /** Speed of bullets, adjusted with the peak velocity of the spaceship. */
    public static final int BULLET_SPEED = Firebase.serverConstants.get("BULLET_SPEED").getAsInt() + PEAK_USER_VEL;

    /** Mass of bullets. */
    public static final int BULLET_MASS = Firebase.serverConstants.get("BULLET_MASS").getAsInt();

    private ArrayList<Coordinate> coords;

    private ArrayList<Integer> spaceShipIds;
    private ArrayList<Integer> asteroidIds;
    private ArrayList<Integer> bulletIds;
    private ArrayList<Integer> blackholeIds;

    private Manager gameEngineManager;

    /**
     * Creates a new GameEngine and initializes all the elements in the game.
     */
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
     * Instantiates all non-user objects
     * @param id    the id of the enemy object
     */
    public void instantiateGameEngine(int id) {
//        write code to spawn meteors and asteroids and blackholes
        int enemyId = this.gameEngineManager.dropEnemy(0, 0, 10, 1, ENEMY_RADIUS, ENEMY_RADIUS, SPACESHIP_MASS, false, 1, id);
        this.spaceShipIds.add(enemyId);
        int asteroidId = this.addElement("ASTEROID", GAME_WIDTH/2, GAME_HEIGHT/2, 900);
        this.asteroidIds.add(asteroidId);
        System.out.println("############################################################# " + enemyId);
    }

    /**
     * This adds an element. NOTE: for adding a spaceShip please use the function addShip()
     * @param type  type of element
     * @param x     x*10 coordinate of that element
     * @param y     y*10 coordinate of that element
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

            case "ASTEROID":
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
     * @param id    the id of the object you want to update
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
        System.out.println(tempCoords.length+"display");
        
        for (int[] element : tempCoords) {
            this.coords.add(new Coordinate("B", element[0], element[1], element[2], element[3]));
        }
    }

    /**
     * This is a helper function that helps one to divide all the Coordinates to different
     * types.
     * @param type this is the type of coordinates you want
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
                    if (this.asteroidIds.contains(coord.id)) {
                        System.out.println(type);
                        retValue.add(coord);
                    }
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
