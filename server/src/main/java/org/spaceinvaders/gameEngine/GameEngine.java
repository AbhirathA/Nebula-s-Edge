package org.spaceinvaders.gameEngine;

import com.physics.Manager;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.util.Coordinate;

import java.util.ArrayList;

public class GameEngine {

    public static final int BLACKHOLE_INNER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_INNER_RADIUS").getAsInt();
    public static final int BLACKHOLE_OUTER_RADIUS = Firebase.serverConstants.get("BLACKHOLE_OUTER_RADIUS").getAsInt();
    public static final int BLACKHOLE_MASS = Firebase.serverConstants.get("BLACKHOLE_MASS").getAsInt();
    public static final int CAMERA_HEIGHT = Firebase.serverConstants.get("CAMERA_HEIGHT").getAsInt();
    public static final int CAMERA_WIDTH = Firebase.serverConstants.get("CAMERA_WIDTH").getAsInt();
    public static final int ENEMY_RADIUS = Firebase.serverConstants.get("ENEMY_RADIUS").getAsInt();
    public static final int GAME_HEIGHT = Firebase.serverConstants.get("GAME_HEIGHT").getAsInt();
    public static final int GAME_WIDTH = Firebase.serverConstants.get("GAME_WIDTH").getAsInt();
    public static final int BIG_ASTEROID_RADIUS = Firebase.serverConstants.get("BIG_ASTEROID_RADIUS").getAsInt();
    public static final int BIG_ASTEROID_MASS = Firebase.serverConstants.get("BIG_ASTEROID_MASS").getAsInt();
    public static final int MEDIUM_ASTEROID_RADIUS = Firebase.serverConstants.get("MEDIUM_ASTEROID_RADIUS").getAsInt();
    public static final int SMALL_ASTEROID_RADIUS = Firebase.serverConstants.get("SMALL_ASTEROID_RADIUS").getAsInt();
    public static final int SPACESHIP_RADIUS = Firebase.serverConstants.get("SPACESHIP_RADIUS").getAsInt();
    public static final int SPACESHIP_MASS = Firebase.serverConstants.get("SPACESHIP_MASS").getAsInt();
    public static final int SPACESHIP_HEALTH = Firebase.serverConstants.get("SPACESHIP_HEALTH").getAsInt();
    public static final int PEAK_USER_VEL = Firebase.serverConstants.get("PEAK_USER_VEL").getAsInt();
    public static final int DRIFT_USER_VEL = Firebase.serverConstants.get("DRIFT_USER_VEL").getAsInt();
    public static final int BULLET_RADIUS = Firebase.serverConstants.get("BULLET_RADIUS").getAsInt();
    public static final int BULLET_LIFE = Firebase.serverConstants.get("BULLET_LIFE").getAsInt(); ;
    public static final int BULLET_SPEED = Firebase.serverConstants.get("BULLET_SPEED").getAsInt() + PEAK_USER_VEL;
    public static final int BULLET_MASS = Firebase.serverConstants.get("BULLET_MASS").getAsInt();

    private ArrayList<Coordinate> coords;

    private ArrayList<Integer> spaceShipIds;
    private ArrayList<Integer> enemyIds;
    private ArrayList<Integer> asteroidIds;
    private ArrayList<Integer> meteorIds;
    private ArrayList<Integer> bulletIds;
    private ArrayList<Integer> blackholeIds;

    private Manager gameEngineManager;

    public GameEngine() {
//         TODO: instantiate variables to save for someting ig
        this.coords = new ArrayList<>();

        this.spaceShipIds = new ArrayList<>();
        this.enemyIds = new ArrayList<>();
        this.asteroidIds = new ArrayList<>();
        this.meteorIds = new ArrayList<>();
        this.bulletIds = new ArrayList<>();
        this.blackholeIds = new ArrayList<>();

        this.gameEngineManager = new Manager(0, 0, 0, GAME_WIDTH, 0, GAME_HEIGHT, 1);
    }

    /**
     * This method should instantiate all non-user objects
     */
    public void instantiateGameEngineObjects() {
//        write code to spawn meteors and asteroids and blackholes

        int asteroidId = this.addElement("ASTEROID", GAME_WIDTH/2 - 500, GAME_HEIGHT/2, 900);
        int meteorId1 = this.addMeteor(GAME_WIDTH/4, GAME_HEIGHT/2, 10, 0);
        int meteorId2 = this.addMeteor(3*GAME_WIDTH/4, GAME_HEIGHT/2, -20, 0);
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

            case "ASTEROID":
                id = this.gameEngineManager.dropAsteroid(x, y, BIG_ASTEROID_RADIUS*2, BIG_ASTEROID_RADIUS*2, BIG_ASTEROID_MASS);
                this.asteroidIds.add(id);
                break;

            case "METEOR":
                id = this.gameEngineManager.dropMeteor(x, y, DRIFT_USER_VEL, DRIFT_USER_VEL, 0, 0, MEDIUM_ASTEROID_RADIUS, MEDIUM_ASTEROID_RADIUS, BIG_ASTEROID_MASS*10);
                this.meteorIds.add(id);
                break;

            case "BLACKHOLE":
                id = this.gameEngineManager.dropBlackHole(x, y, BLACKHOLE_INNER_RADIUS, BLACKHOLE_OUTER_RADIUS, BLACKHOLE_MASS);
                this.blackholeIds.add(id);
                break;
        }

        return id;
    }

    public int addMeteor(int x, int y, int vx, int vy) {
        int id = this.gameEngineManager.dropMeteor(x, y, vx, vy, 0, 0, BIG_ASTEROID_RADIUS, BIG_ASTEROID_RADIUS, BIG_ASTEROID_MASS);
        this.meteorIds.add(id);
        return id;
    }

    public int addEnemy(int x, int y, int id) {
        int enemyId = this.gameEngineManager.dropEnemy(x, y, 10, 1, ENEMY_RADIUS, ENEMY_RADIUS, SPACESHIP_MASS, false, -1, id);
        this.enemyIds.add(enemyId);
        return enemyId;
    }

    public int addBlackhole(int x, int y){
        int blackholeId = this.gameEngineManager.dropBlackHole();
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
        System.out.println(tempCoords.length+"display");
        
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
        return this.addElement("SHIP", GAME_WIDTH/2 + 2*BIG_ASTEROID_RADIUS - 500, GAME_HEIGHT/2, 900);
    }
}
