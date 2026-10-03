package com.spaceinvaders.web;

import com.physics.PhysicsWorld;
import org.teavm.jso.JSBody;
import org.teavm.jso.typedarrays.Int32Array;

/**
 * WasmPhysicsWorld.java
 * <br>
 * The browser twin of {@link com.physics.Manager}: the same C++ engine, compiled to
 * WebAssembly with Emscripten (src/main/native/web/physics_wasm.cpp) and loaded by
 * physics-loader.js as the global {@code NebulaPhysics}. Each call crosses from the
 * TeaVM-compiled Java into the Wasm module; a world is identified by its C++ pointer.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 10/03/2026
 */
public class WasmPhysicsWorld implements PhysicsWorld {

    private int handle;

    public WasmPhysicsWorld(int accX, int accY, int lft, int rt, int tp, int bt, int t) {
        this.handle = create(accX, accY, lft, rt, tp, bt, t);
    }

    /**
     * Whether the WebAssembly module has finished loading.
     * @return true once worlds can be created
     */
    @JSBody(script = "return !!window.NebulaPhysics;")
    public static native boolean isReady();

    @JSBody(params = {"accX", "accY", "lft", "rt", "tp", "bt", "t"},
            script = "return NebulaPhysics._nebula_create(accX, accY, lft, rt, tp, bt, t);")
    private static native int create(int accX, int accY, int lft, int rt, int tp, int bt, int t);

    @JSBody(params = {"h"}, script = "NebulaPhysics._nebula_destroy(h);")
    private static native void destroy(int h);

    @JSBody(params = {"h"}, script = "NebulaPhysics._nebula_update(h);")
    private static native void update(int h);

    @JSBody(params = {"h", "x", "y", "peakV", "driftV", "angle", "thrust", "thrustPersistance", "movePersistance",
            "coolDown", "accX", "accY", "innerRad", "outerRad", "mass", "health", "bulletSpeed", "bulletLife"},
            script = "return NebulaPhysics._nebula_drop_user(h, x, y, peakV, driftV, angle, thrust, thrustPersistance,"
                    + " movePersistance, coolDown, accX, accY, innerRad, outerRad, mass, health, bulletSpeed, bulletLife);")
    private static native int dropUser(int h, int x, int y, int peakV, int driftV, int angle, int thrust,
                                       int thrustPersistance, int movePersistance, int coolDown, int accX, int accY,
                                       int innerRad, int outerRad, int mass, int health, int bulletSpeed, int bulletLife);

    @JSBody(params = {"h", "id", "innerRadius", "outerRadius", "mass"},
            script = "return NebulaPhysics._nebula_shoot(h, id, innerRadius, outerRadius, mass);")
    private static native int shoot(int h, int id, int innerRadius, int outerRadius, int mass);

    @JSBody(params = {"h", "x", "y", "innerRad", "outerRad", "mass"},
            script = "return NebulaPhysics._nebula_drop_asteroid(h, x, y, innerRad, outerRad, mass);")
    private static native int dropAsteroid(int h, int x, int y, int innerRad, int outerRad, int mass);

    @JSBody(params = {"h", "x", "y", "innerRad", "outerRad", "mass"},
            script = "return NebulaPhysics._nebula_drop_black_hole(h, x, y, innerRad, outerRad, mass);")
    private static native int dropBlackHole(int h, int x, int y, int innerRad, int outerRad, int mass);

    @JSBody(params = {"h", "x", "y", "v", "res", "innerRad", "outerRad", "mass", "startX", "startSign", "aim"},
            script = "return NebulaPhysics._nebula_drop_enemy(h, x, y, v, res, innerRad, outerRad, mass,"
                    + " startX ? 1 : 0, startSign, aim);")
    private static native int dropEnemy(int h, int x, int y, int v, int res, int innerRad, int outerRad, int mass,
                                        boolean startX, int startSign, int aim);

    @JSBody(params = {"h", "x", "y", "vX", "vY", "accX", "accY", "innerRad", "outerRad", "mass"},
            script = "return NebulaPhysics._nebula_drop_meteor(h, x, y, vX, vY, accX, accY, innerRad, outerRad, mass);")
    private static native int dropMeteor(int h, int x, int y, int vX, int vY, int accX, int accY, int innerRad,
                                         int outerRad, int mass);

    @JSBody(params = {"h", "x", "y", "radius", "healthIncrease"},
            script = "return NebulaPhysics._nebula_drop_health_power_up(h, x, y, radius, healthIncrease);")
    private static native int dropHealthPowerUp(int h, int x, int y, int radius, int healthIncrease);

    @JSBody(params = {"h", "x", "y", "radius", "speedBoost", "lifeBoost", "duration"},
            script = "return NebulaPhysics._nebula_drop_bullet_boost_power_up(h, x, y, radius, speedBoost, lifeBoost, duration);")
    private static native int dropBulletBoostPowerUp(int h, int x, int y, int radius, int speedBoost, int lifeBoost,
                                                     int duration);

    @JSBody(params = {"h", "x", "y", "radius", "pointScale", "duration"},
            script = "return NebulaPhysics._nebula_drop_increase_points_power_up(h, x, y, radius, pointScale, duration);")
    private static native int dropIncreasePointsPowerUp(int h, int x, int y, int radius, int pointScale, int duration);

    @JSBody(params = {"h", "id"}, script = "return NebulaPhysics._nebula_get_health(h, id);")
    private static native int getHealth(int h, int id);

    @JSBody(params = {"h", "id"}, script = "return NebulaPhysics._nebula_get_points(h, id);")
    private static native int getPoints(int h, int id);

    // Returns a copy of the flattened {id, x, y, orientation} rows
    @JSBody(params = {"h", "lowerX", "lowerY", "upperX", "upperY"},
            script = "var n = NebulaPhysics._nebula_display(h, lowerX, lowerY, upperX, upperY);"
                    + " var p = NebulaPhysics._nebula_display_buffer() >> 2;"
                    + " return NebulaPhysics.HEAP32.slice(p, p + n * 4);")
    private static native Int32Array display(int h, int lowerX, int lowerY, int upperX, int upperY);

    @JSBody(params = {"h", "id"}, script = "NebulaPhysics._nebula_forward(h, id);")
    private static native void forward(int h, int id);

    @JSBody(params = {"h", "id"}, script = "NebulaPhysics._nebula_stop(h, id);")
    private static native void stop(int h, int id);

    @JSBody(params = {"h", "id"}, script = "NebulaPhysics._nebula_thrust(h, id);")
    private static native void thrust(int h, int id);

    @JSBody(params = {"h", "id"}, script = "NebulaPhysics._nebula_left(h, id);")
    private static native void left(int h, int id);

    @JSBody(params = {"h", "id"}, script = "NebulaPhysics._nebula_right(h, id);")
    private static native void right(int h, int id);

    @JSBody(params = {"h", "id"}, script = "NebulaPhysics._nebula_remove(h, id);")
    private static native void remove(int h, int id);

    @Override
    public void update() {
        if (handle != 0) update(handle);
    }

    @Override
    public int dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance,
                        int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass,
                        int health, int bulletSpeed, int bulletLife) {
        return dropUser(handle, x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown,
                accX, accY, innerRad, outerRad, mass, health, bulletSpeed, bulletLife);
    }

    @Override
    public int shoot(int id, int innerRadius, int outerRadius, int mass) {
        return shoot(handle, id, innerRadius, outerRadius, mass);
    }

    @Override
    public int dropAsteroid(int x, int y, int innerRad, int outerRad, int mass) {
        return dropAsteroid(handle, x, y, innerRad, outerRad, mass);
    }

    @Override
    public int dropBlackHole(int x, int y, int innerRad, int outerRad, int mass) {
        return dropBlackHole(handle, x, y, innerRad, outerRad, mass);
    }

    @Override
    public int dropEnemy(int x, int y, int v, int res, int innerRad, int outerRad, int mass, boolean startX,
                         int startSign, int aim) {
        return dropEnemy(handle, x, y, v, res, innerRad, outerRad, mass, startX, startSign, aim);
    }

    @Override
    public int dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass) {
        return dropMeteor(handle, x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
    }

    @Override
    public int dropHealthPowerUp(int x, int y, int radius, int healthIncrease) {
        return dropHealthPowerUp(handle, x, y, radius, healthIncrease);
    }

    @Override
    public int dropBulletBoostPowerUp(int x, int y, int radius, int speedBoost, int lifeBoost, int duration) {
        return dropBulletBoostPowerUp(handle, x, y, radius, speedBoost, lifeBoost, duration);
    }

    @Override
    public int dropIncreasePointsPowerUp(int x, int y, int radius, int pointScale, int duration) {
        return dropIncreasePointsPowerUp(handle, x, y, radius, pointScale, duration);
    }

    @Override
    public int getHealth(int id) {
        return getHealth(handle, id);
    }

    @Override
    public int getPoints(int id) {
        return getPoints(handle, id);
    }

    @Override
    public int[][] display(int lowerX, int lowerY, int upperX, int upperY) {
        if (handle == 0) return new int[0][];
        Int32Array flat = display(handle, lowerX, lowerY, upperX, upperY);
        int[][] rows = new int[flat.getLength() / 4][];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new int[]{flat.get(i * 4), flat.get(i * 4 + 1), flat.get(i * 4 + 2), flat.get(i * 4 + 3)};
        }
        return rows;
    }

    @Override
    public void forward(int id) {
        forward(handle, id);
    }

    @Override
    public void stop(int id) {
        stop(handle, id);
    }

    @Override
    public void thrust(int id) {
        thrust(handle, id);
    }

    @Override
    public void left(int id) {
        left(handle, id);
    }

    @Override
    public void right(int id) {
        right(handle, id);
    }

    @Override
    public void remove(int id) {
        remove(handle, id);
    }

    @Override
    public void dispose() {
        if (handle != 0) {
            destroy(handle);
            handle = 0;
        }
    }
}
