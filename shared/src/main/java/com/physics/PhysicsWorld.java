package com.physics;

/**
 * PhysicsWorld.java
 * <br>
 * The operations the game needs from the C++ physics engine. The desktop server
 * implements it with {@link Manager} (JNI); the browser build implements it with
 * the same C++ code compiled to WebAssembly. Coordinates are engine units
 * (10 per on-screen pixel) and angles are tenths of a degree.
 * @author Dedeepya
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @version 1.0
 * @since 11/27/2024
 */
public interface PhysicsWorld {

    /**
     * Creates an empty world.
     */
    interface Factory {
        /**
         * @param accX global acceleration along x
         * @param accY global acceleration along y
         * @param lft  left boundary
         * @param rt   right boundary
         * @param tp   top boundary
         * @param bt   bottom boundary
         * @param t    time step per update
         * @return the new world
         */
        PhysicsWorld create(int accX, int accY, int lft, int rt, int tp, int bt, int t);
    }

    void update();

    int dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance,
                 int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass,
                 int health, int bulletSpeed, int bulletLife);

    int shoot(int id, int innerRadius, int outerRadius, int mass);

    int dropAsteroid(int x, int y, int innerRad, int outerRad, int mass);

    int dropBlackHole(int x, int y, int innerRad, int outerRad, int mass);

    int dropEnemy(int x, int y, int v, int res, int innerRad, int outerRad, int mass, boolean startX,
                  int startSign, int aim);

    int dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);

    int dropHealthPowerUp(int x, int y, int radius, int healthIncrease);

    int dropBulletBoostPowerUp(int x, int y, int radius, int speedBoost, int lifeBoost, int duration);

    int dropIncreasePointsPowerUp(int x, int y, int radius, int pointScale, int duration);

    int getHealth(int id);

    int getPoints(int id);

    /**
     * Returns every object inside the box as {@code {id, x, y, orientation}} rows.
     */
    int[][] display(int lowerX, int lowerY, int upperX, int upperY);

    void forward(int id);

    void stop(int id);

    void thrust(int id);

    void left(int id);

    void right(int id);

    /**
     * Destroys an object; it disappears on the next update.
     */
    void remove(int id);

    /**
     * Frees the world. Further calls are no-ops.
     */
    void dispose();
}
