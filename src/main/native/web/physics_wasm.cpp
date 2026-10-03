// C API over the physics engine for the browser build.
//
// The desktop server talks to Manager through JNI (Manager_JNI.cpp). In the browser the
// same engine is compiled to WebAssembly with Emscripten and these functions are called
// from the TeaVM-compiled Java client (see client/web/.../WasmPhysicsWorld.java).
// A world is identified by its Manager pointer, which is a plain int in wasm32.

#include "../Manager.h"
#include <emscripten/emscripten.h>
#include <vector>

namespace
{
    // display() results are flattened here as {id, x, y, orientation} * n
    std::vector<int> displayBuffer;
}

extern "C"
{
    EMSCRIPTEN_KEEPALIVE Manager *nebula_create(int accX, int accY, int lft, int rt, int tp, int bt, int t)
    {
        return new Manager(accX, accY, lft, rt, tp, bt, t);
    }

    EMSCRIPTEN_KEEPALIVE void nebula_destroy(Manager *m) { delete m; }

    EMSCRIPTEN_KEEPALIVE void nebula_update(Manager *m) { m->update(); }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_user(Manager *m, int x, int y, int peakV, int driftV, int angle, int thrust,
                                              int thrustPersistance, int movePersistance, int coolDown, int accX,
                                              int accY, int innerRad, int outerRad, int mass, int health,
                                              int bulletSpeed, int bulletLife)
    {
        return m->dropUser(x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown, accX,
                           accY, innerRad, outerRad, mass, health, bulletSpeed, bulletLife);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_shoot(Manager *m, int id, int innerRadius, int outerRadius, int mass)
    {
        return m->shoot(id, innerRadius, outerRadius, mass);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_asteroid(Manager *m, int x, int y, int innerRad, int outerRad, int mass)
    {
        return m->dropAsteroid(x, y, innerRad, outerRad, mass);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_black_hole(Manager *m, int x, int y, int innerRad, int outerRad, int mass)
    {
        return m->dropBlackHole(x, y, innerRad, outerRad, mass);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_enemy(Manager *m, int x, int y, int v, int res, int innerRad, int outerRad,
                                               int mass, int startX, int startSign, int aim)
    {
        return m->dropEnemy(x, y, v, res, innerRad, outerRad, mass, startX != 0, startSign, aim);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_meteor(Manager *m, int x, int y, int vX, int vY, int accX, int accY,
                                                int innerRad, int outerRad, int mass)
    {
        return m->dropMeteor(x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_health_power_up(Manager *m, int x, int y, int radius, int healthIncrease)
    {
        return m->dropHealthPowerUp(x, y, radius, healthIncrease);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_bullet_boost_power_up(Manager *m, int x, int y, int radius, int speedBoost,
                                                               int lifeBoost, int duration)
    {
        return m->dropBulletBoostPowerUp(x, y, radius, speedBoost, lifeBoost, duration);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_drop_increase_points_power_up(Manager *m, int x, int y, int radius,
                                                                  int pointScale, int duration)
    {
        return m->dropIncreasePointsPowerUp(x, y, radius, pointScale, duration);
    }

    EMSCRIPTEN_KEEPALIVE int nebula_get_health(Manager *m, int id) { return m->getHealth(id); }

    EMSCRIPTEN_KEEPALIVE int nebula_get_points(Manager *m, int id) { return m->getPoints(id); }

    // Returns the number of objects; read them from nebula_display_buffer()
    EMSCRIPTEN_KEEPALIVE int nebula_display(Manager *m, int lowerX, int lowerY, int upperX, int upperY)
    {
        std::vector<std::vector<int>> rows = m->display(lowerX, lowerY, upperX, upperY);
        displayBuffer.clear();
        for (auto &row : rows)
            displayBuffer.insert(displayBuffer.end(), row.begin(), row.end());
        return static_cast<int>(rows.size());
    }

    EMSCRIPTEN_KEEPALIVE int *nebula_display_buffer() { return displayBuffer.data(); }

    EMSCRIPTEN_KEEPALIVE void nebula_forward(Manager *m, int id) { m->forward(id); }

    EMSCRIPTEN_KEEPALIVE void nebula_stop(Manager *m, int id) { m->stop(id); }

    EMSCRIPTEN_KEEPALIVE void nebula_thrust(Manager *m, int id) { m->thrust(id); }

    EMSCRIPTEN_KEEPALIVE void nebula_left(Manager *m, int id) { m->left(id); }

    EMSCRIPTEN_KEEPALIVE void nebula_right(Manager *m, int id) { m->right(id); }

    EMSCRIPTEN_KEEPALIVE void nebula_remove(Manager *m, int id) { m->remove(id); }
}
