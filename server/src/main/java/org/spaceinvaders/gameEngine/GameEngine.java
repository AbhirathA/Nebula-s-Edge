package org.spaceinvaders.gameEngine;

import com.physics.Manager;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.Coordinate;

import java.util.ArrayList;

/**
 * GameEngine.java
 * <br>
 * The GameEngine class manages the game logic for a space invaders-like game.
 * It is responsible for instantiating game objects (spaceships, enemies, asteroids, meteors, etc.)
 * and updating their states. The game mechanics include movement, collisions, and physics.
 * @author Dedeepya
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @version 1.0
 * @since 11/27/2024
 */
public class GameEngine {

    /**
     * The inner radius of the black hole's gravitational pull.
     */
    public static final int BLACKHOLE_INNER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_INNER_RADIUS").getAsInt();

    /**
     * The outer radius of the black hole's gravitational influence.
     */
    public static final int BLACKHOLE_OUTER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_OUTER_RADIUS").getAsInt() * 30;

    /**
     * The mass of the black hole.
     */
    public static final int BLACKHOLE_MASS = Firebase.serverConstants.get("BLACKHOLE_MASS").getAsInt();

    /**
     * The height of the camera's viewing area.
     */
    public static final int CAMERA_HEIGHT = Firebase.serverConstants.get("CAMERA_HEIGHT").getAsInt();

    /**
     * The width of the camera's viewing area.
     */
    public static final int CAMERA_WIDTH = Firebase.serverConstants.get("CAMERA_WIDTH").getAsInt();

    /**
     * The radius of the enemy objects.
     */
    public static final int ENEMY_RADIUS = Firebase.serverConstants.get("ENEMY_RADIUS").getAsInt();

    /**
     * The height of the game world.
     */
    public static final int GAME_HEIGHT = Firebase.serverConstants.get("GAME_HEIGHT").getAsInt();

    /**
     * The width of the game world.
     */
    public static final int GAME_WIDTH = Firebase.serverConstants.get("GAME_WIDTH").getAsInt();

    /**
     * The radius of a large asteroid.
     */
    public static final int BIG_ASTEROID_RADIUS = Firebase.serverConstants.get("BIG_ASTEROID_RADIUS").getAsInt();

    /**
     * The mass of a large asteroid.
     */
    public static final int BIG_ASTEROID_MASS = Firebase.serverConstants.get("BIG_ASTEROID_MASS").getAsInt();

    /**
     * The radius of a medium-sized asteroid.
     */
    public static final int MEDIUM_ASTEROID_RADIUS = Firebase.serverConstants.get("MEDIUM_ASTEROID_RADIUS").getAsInt();

    /**
     * The radius of a small asteroid.
     */
    public static final int SMALL_ASTEROID_RADIUS = Firebase.serverConstants.get("SMALL_ASTEROID_RADIUS").getAsInt();

    /**
     * The radius of a spaceship.
     */
    public static final int SPACESHIP_RADIUS = Firebase.serverConstants.get("SPACESHIP_RADIUS").getAsInt();

    /**
     * The mass of a spaceship.
     */
    public static final int SPACESHIP_MASS = Firebase.serverConstants.get("SPACESHIP_MASS").getAsInt();

    /**
     * The health points of the spaceship.
     */
    public static final int SPACESHIP_HEALTH = Firebase.serverConstants.get("SPACESHIP_HEALTH").getAsInt();

    /**
     * The peak velocity of the user-controlled spaceship.
     */
    public static final int PEAK_USER_VEL = Firebase.serverConstants.get("PEAK_USER_VEL").getAsInt();

    /**
     * The drifting velocity of the user-controlled spaceship.
     */
    public static final int DRIFT_USER_VEL = Firebase.serverConstants.get("DRIFT_USER_VEL").getAsInt();

    /**
     * The radius of bullets fired by the spaceship.
     */
    public static final int BULLET_RADIUS = Firebase.serverConstants.get("BULLET_RADIUS").getAsInt();

    /**
     * The lifetime of a bullet in the game (in seconds).
     */
    public static final int BULLET_LIFE = Firebase.serverConstants.get("BULLET_LIFE").getAsInt();

    /**
     * The speed of the bullet, adjusted by the spaceship's peak velocity.
     */
    public static final int BULLET_SPEED = Firebase.serverConstants.get("BULLET_SPEED").getAsInt() + PEAK_USER_VEL;

    /**
     * The mass of a bullet.
     */
    public static final int BULLET_MASS = Firebase.serverConstants.get("BULLET_MASS").getAsInt();

    private ArrayList<Coordinate> coords;

    private ArrayList<Integer> spaceShipIds;
    private ArrayList<Integer> enemyIds;
    private ArrayList<Integer> asteroidIds;
    private ArrayList<Integer> meteorIds;
    private ArrayList<Integer> bulletIds;
    private ArrayList<Integer> blackholeIds;
    private ArrayList<Integer> powerUpHIds;
    private ArrayList<Integer> powerUpBIds;
    private ArrayList<Integer> powerUpPIds;
    private ArrayList<Integer> deadIds;

    private Manager gameEngineManager;

    /**
     * Constructor for the GameEngine class. Initializes the game objects' lists
     * and sets up the game engine manager with the game world dimensions.
     */
    public GameEngine() {
        this.coords = new ArrayList<>();

        this.spaceShipIds = new ArrayList<>();
        this.enemyIds = new ArrayList<>();
        this.asteroidIds = new ArrayList<>();
        this.meteorIds = new ArrayList<>();
        this.bulletIds = new ArrayList<>();
        this.blackholeIds = new ArrayList<>();
        this.powerUpHIds = new ArrayList<>();
        this.powerUpBIds = new ArrayList<>();
        this.powerUpPIds = new ArrayList<>();
        this.deadIds = new ArrayList<>();

        this.gameEngineManager = new Manager(0, 0, 0, GAME_WIDTH, 0, GAME_HEIGHT, 1);
    }

    /**
     * Instantiates the non-user objects (like asteroids, meteors, and blackholes) in the game world.
     */
    public void instantiateGameEngineObjects() {
//        write code to spawn meteors and asteroids and blackholes

        System.out.println(BLACKHOLE_INNER_RADIUS + " " + BLACKHOLE_OUTER_RADIUS+" " + "Gaaaaaathik");

//        int asteroidId = this.addElement("ASTEROID", GAME_WIDTH/2 - 500, GAME_HEIGHT/2, 900);
        int meteorId1 = this.powerUpBullet(GAME_WIDTH/4, GAME_HEIGHT/2);
        int meteorId2 = this.powerUpBullet(3*GAME_WIDTH/4, GAME_HEIGHT/2);
        //int blackhole = this.addBlackhole(GAME_WIDTH/4-1000, GAME_HEIGHT/2);

        int asteroidId = this.addElement("ASTEROID", 460, -360, 900);
        asteroidId = this.addElement("ASTEROID", 2480, -2080, 900);
        asteroidId = this.addElement("ASTEROID", 5040, -440, 900);
        asteroidId = this.addElement("ASTEROID", 7590, -1120, 900);
        asteroidId = this.addElement("ASTEROID", 10860, -1240, 900);
        asteroidId = this.addElement("ASTEROID", 6960, -2830, 900);
        asteroidId = this.addElement("ASTEROID", 9620, -3310, 900);
        asteroidId = this.addElement("ASTEROID", 10340, -4760, 900);
        asteroidId = this.addElement("ASTEROID", 1030, -3620, 900);
        asteroidId = this.addElement("ASTEROID", 2030, -3810, 900);
        asteroidId = this.addElement("ASTEROID", 1440, -4660, 900);
        asteroidId = this.addElement("ASTEROID", 2360, -4610, 900);
        asteroidId = this.addElement("ASTEROID", 1180, -5620, 900);
        asteroidId = this.addElement("ASTEROID", 2800, -5530, 900);
        asteroidId = this.addElement("ASTEROID", 5710, -5880, 900);
        asteroidId = this.addElement("ASTEROID", 8240, -5590, 900);
        asteroidId = this.addElement("ASTEROID", 9580, -5700, 900);
        int PBId1 = this.powerUpBullet(7310, -3610);
        PBId1 = this.powerUpBullet(1420, -840);
        int PBId2 = this.powerUpHealth(1800, -830);
        PBId2 = this.powerUpHealth(1630, -980);
        PBId2 = this.powerUpHealth(7370, -1110);
        PBId2 = this.powerUpHealth(8700, -6230);
        PBId2 = this.powerUpHealth(1960, -6180);
        int PBId3 = this.powerUpPoint(1370, -4060);
        PBId3 = this.powerUpPoint(5380, -5800);
        PBId3 = this.powerUpPoint(10990, -5730);
        PBId3 = this.powerUpPoint(6360, -1010);
        int blackholeId = this.addBlackhole(1640, -450);
        blackholeId = this.addBlackhole(2500, -950);
        blackholeId = this.addBlackhole(1660, -1580);
        blackholeId = this.addBlackhole(6270, -1370);
        blackholeId = this.addBlackhole(11310, -5970);
        blackholeId = this.addBlackhole(850, -1100);
        int meteorId = this.addMeteor(4020, -620, -DRIFT_USER_VEL, 2*DRIFT_USER_VEL);
        meteorId = this.addMeteor(5880, -550, DRIFT_USER_VEL, 2*DRIFT_USER_VEL);
        meteorId = this.addMeteor(10100, -530, DRIFT_USER_VEL, -DRIFT_USER_VEL);
        meteorId = this.addMeteor(8890, -2140, -DRIFT_USER_VEL / 2, -2*DRIFT_USER_VEL);
        meteorId = this.addMeteor(5210, -2110, 0*DRIFT_USER_VEL, -2*DRIFT_USER_VEL);
        meteorId = this.addMeteor(3360, -2700, DRIFT_USER_VEL, -DRIFT_USER_VEL);
        meteorId = this.addMeteor(560, -4360, -2*DRIFT_USER_VEL, 0*DRIFT_USER_VEL);
        meteorId = this.addMeteor(9140, -4930, -2*DRIFT_USER_VEL, DRIFT_USER_VEL / 2);
    }

    /**
     * Adds a game element (spaceship, asteroid, meteor, or blackhole) to the game world.
     *
     * @param type  the type of element to add (SHIP, ASTEROID, METEOR, BLACKHOLE)
     * @param x     the x-coordinate of the element
     * @param y     the y-coordinate of the element
     * @param angle the angle of the element
     * @return the ID of the newly created element
     */
    public int addElement(String type, int x, int y, int angle) {
        int id = 0;
        switch (type) {
            case "SHIP":
                id = this.gameEngineManager.dropUser(x, y, PEAK_USER_VEL, DRIFT_USER_VEL, angle, 10, 10, 25, 144, 0, 0, SPACESHIP_RADIUS, SPACESHIP_RADIUS, SPACESHIP_MASS, SPACESHIP_HEALTH, BULLET_SPEED, BULLET_LIFE);
                this.spaceShipIds.add(id);
                this.addEnemy(3270, -1280, id);
                this.addEnemy(9280, -380, id);
                this.addEnemy(8500, -1530, id);
                this.addEnemy(7460, -2030, id);
                this.addEnemy(11190, -2240, id);
                this.addEnemy(900, -2680, id);
                this.addEnemy(3750, -4350, id);
                this.addEnemy(4260, -5390, id);
                this.addEnemy(6840, -5600, id);
                this.addEnemy(7390, -5090, id);
                this.addEnemy(11350, -4870, id);
                break;

            case "ASTEROID":
                id = this.gameEngineManager.dropAsteroid(x, y, BIG_ASTEROID_RADIUS, BIG_ASTEROID_RADIUS, BIG_ASTEROID_MASS);
                this.asteroidIds.add(id);
                break;

            case "METEOR":
                id = this.gameEngineManager.dropMeteor(x, y, DRIFT_USER_VEL, DRIFT_USER_VEL, 0, 0, MEDIUM_ASTEROID_RADIUS, MEDIUM_ASTEROID_RADIUS, BIG_ASTEROID_MASS*10);
                this.meteorIds.add(id);
                break;

            case "BLACKHOLES":
                id = this.gameEngineManager.dropBlackHole(x, y, BLACKHOLE_INNER_RADIUS, BLACKHOLE_OUTER_RADIUS, BLACKHOLE_MASS);
                this.blackholeIds.add(id);
                break;
        }

        return id;
    }

    public int powerUpHealth(int x, int y) {
        int id = this.gameEngineManager.dropHealthPowerUp(x, y, BULLET_RADIUS*2, 2);
        this.powerUpHIds.add(id);
        return id;
    }

    public int powerUpBullet(int x, int y) {
        int id = this.gameEngineManager.dropBulletBoostPowerUp(x, y, BULLET_RADIUS*2, BULLET_SPEED*2, BULLET_LIFE*2, 600);
        this.powerUpBIds.add(id);
        return id;
    }

    public int powerUpPoint(int x, int y) {
        int id = this.gameEngineManager.dropIncreasePointsPowerUp(x, y, BULLET_RADIUS*2, 2, 1000);
        this.powerUpPIds.add(id);
        return id;
    }
    /**
     * Adds a meteor to the game world.
     *
     * @param x  the x-coordinate of the meteor
     * @param y  the y-coordinate of the meteor
     * @param vx the x-velocity of the meteor
     * @param vy the y-velocity of the meteor
     * @return the ID of the newly created meteor
     */
    public int addMeteor(int x, int y, int vx, int vy) {
        int id = this.gameEngineManager.dropMeteor(x, y, vx, vy, 0, 0, BIG_ASTEROID_RADIUS, BIG_ASTEROID_RADIUS, BIG_ASTEROID_MASS);
        this.meteorIds.add(id);
        return id;
    }

    /**
     * Adds an enemy to the game world.
     *
     * @param x   the x-coordinate of the enemy
     * @param y   the y-coordinate of the enemy
     * @param id the ID of the enemy
     * @return the ID of the newly created enemy
     */
    public int addEnemy(int x, int y, int id) {
        int enemyId = this.gameEngineManager.dropEnemy(x, y, 10, 1, ENEMY_RADIUS, ENEMY_RADIUS, SPACESHIP_MASS, false, -1, id);
        this.enemyIds.add(enemyId);
        return enemyId;
    }

    /**
     * Adds a black hole to the game world.
     *
     * @param x     the x-coordinate of the black hole
     * @param y     the y-coordinate of the black hole
     * @return  the ID of the newly created black hole
     */
    public int addBlackhole(int x, int y){
        int blackholeId = this.gameEngineManager.dropBlackHole(x, y, BLACKHOLE_INNER_RADIUS, BLACKHOLE_OUTER_RADIUS, BLACKHOLE_MASS);
        this.blackholeIds.add(blackholeId);
        return blackholeId;
    }

    /**
     * Updates the state of an object in the game based on the given state string.
     *
     * @param id    the ID of the object to update
     * @param state the new state for the object
     */
    public void updateState(int id, String state) {
        if (state.contains("LEFT")) this.gameEngineManager.left(id);
        else if (state.contains("RIGHT")) this.gameEngineManager.right(id);
        if (state.contains("FORWARD")) this.gameEngineManager.forward(id);
        else if (state.contains("BACKWARD")) this.gameEngineManager.stop(id);
        if (state.contains(("BULLET"))) this.bulletIds.add(this.gameEngineManager.shoot(id, BULLET_RADIUS, BULLET_RADIUS, BULLET_MASS));
    }

    /**
     * Updates the game engine's state for one frame.
     */
    public void update() {
        this.gameEngineManager.update();
    }

    /**
     * This function removes all ids from all the mappings that are not part of totalIds
     * Basically removes all the ids that are dead
     * @param totalIds  all the ids that are still alive
     */
    private void removeUselessIds(ArrayList<Integer> totalIds) {

        // need to broadcast one last time
        for (int i = 0; i < spaceShipIds.size(); i++) {
            if (!totalIds.contains(spaceShipIds.get(i))) {
                this.deadIds.add(spaceShipIds.get(i));
                this.spaceShipIds.remove(i);
                i--;
            }
        }

        this.enemyIds.removeIf(x -> !totalIds.contains(x));
        this.asteroidIds.removeIf(x -> !totalIds.contains(x));
        this.meteorIds.removeIf(x -> !totalIds.contains(x));
        this.bulletIds.removeIf(x -> !totalIds.contains(x));
        this.blackholeIds.removeIf(x -> !totalIds.contains(x));
        this.powerUpBIds.removeIf(x -> !totalIds.contains(x));
        this.powerUpHIds.removeIf(x -> !totalIds.contains(x));
        this.powerUpPIds.removeIf(x -> !totalIds.contains(x));
    }

    /**
     * Retrieves all the coordinates of objects in the game.
     */
    public void getAllCoords() {
        this.coords = new ArrayList<>();
        int[][] tempCoords = this.gameEngineManager.display(0, GAME_HEIGHT, GAME_WIDTH, 0);
        for(int i=0; i<tempCoords.length;i++) System.out.println(tempCoords[i][3]+" display");

        ArrayList<Integer> totalIds = new ArrayList<>();
        for (int[] element : tempCoords) {
            this.coords.add(new Coordinate("B", element[0], element[1], element[2], element[3]));
            totalIds.add(element[0]);
        }

        // remove ids that are not being used
        removeUselessIds(totalIds);
    }

    /**
     * Retrieves a filtered list of coordinates based on the provided object type.
     *
     * @param type the type of objects to display (SHIP, ENEMY, ASTEROID, METEOR, BULLET, BLACKHOLE)
     * @return an ArrayList of coordinates for the specified type
     */
    public ArrayList<Coordinate> display(String type) {
        ArrayList<Coordinate> retValue = new ArrayList<>();

        switch (type) {
            case "SHIP":
                for (Coordinate coord : this.coords) {
                    if (this.spaceShipIds.contains(coord.id)) {
                        coord.health = this.gameEngineManager.getHealth(coord.id);
                        coord.point = this.gameEngineManager.getPoints(coord.id);
                        retValue.add(coord);
                    }
                }
                break;

            case "ENEMY":
                for (Coordinate coord : this.coords) {
                    if (this.enemyIds.contains(coord.id)) {
                        retValue.add(coord);
                    }
                }
                break;

            case "ASTEROID":
                for (Coordinate coord : this.coords) {
                    if (this.asteroidIds.contains(coord.id)) {
                        retValue.add(coord);
                    }
                }
                break;

            case "METEOR":
                for (Coordinate coord : this.coords) {
                    if (this.meteorIds.contains(coord.id)) {
                        coord.type = "M";
                        System.out.println(coord.type);
                        retValue.add(coord);
                    }
                }
                break;

            case "BULLET":
                for (Coordinate coord : this.coords) {
                    if (this.bulletIds.contains(coord.id)) retValue.add(coord);
                }
                break;

            case "BLACKHOLES":
                for (Coordinate coord : this.coords) {
                    if (this.blackholeIds.contains(coord.id)) retValue.add(coord);
                }
                break;
            case "POWERUPH":
                for (Coordinate coord : this.coords) {
                    if (this.powerUpHIds.contains(coord.id)) {
                        coord.type = "health";
                        retValue.add(coord);
                    }
                }
                break;
            case "POWERUPP":
                for (Coordinate coord : this.coords) {
                    if (this.powerUpPIds.contains(coord.id)){
                        coord.type = "bonus";
                        retValue.add(coord);
                    }
                }
                break;
            case "POWERUPB":
                for (Coordinate coord : this.coords) {
                    if (this.powerUpBIds.contains(coord.id)) {
                        coord.type = "attack";
                        retValue.add(coord);
                    }
                }
                break;

        }

        return retValue;
    }

    /**
     * Wrapper function to create a spaceship.
     *
     * @return the ID of the newly created spaceship
     */
    public int addShip() {
        return this.addElement("SHIP", GAME_WIDTH/2 + 2*BIG_ASTEROID_RADIUS - 500, GAME_HEIGHT/2, 900);
    }
}
