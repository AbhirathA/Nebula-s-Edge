//
// Created by ibrahim on 27/11/24.
//

#pragma once

#include "velVerlet.h"
#include "Lifetime.h"

class Bullet : public velVerlet
{
protected:
    Lifetime life;
    UserObj *shooter;

public:
    Bullet(int id, int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass, int maxLife, UserObj *shooter) : velVerlet(
                                                                                                                                                id, x, y, vX, vY, accX, accY, innerRad, outerRad, mass),
                                                                                                                                            life(maxLife, [this]() -> void
                                                                                                                                                 { return this->selfDestruct(); })
    {
        this->shooter = shooter;
    }

    virtual bool checkCollision(Obj *obj) { return obj->checkCollision(this); }

    virtual bool checkCollision(Asteroid *obj);
    virtual bool checkCollision(BlackholeObject *obj);
    virtual bool checkCollision(Meteor *obj);
    virtual bool checkCollision(Flare *obj);
    virtual bool checkCollision(PowerUp *obj);
    virtual bool checkCollision(UserObj *obj);
    virtual bool checkCollision(Enemy *obj);
    bool checkCollision(Bullet *obj);

    bool collisionCorrection(Obj *obj)
    {
        return obj->collisionCorrection(this);
    }

    virtual bool collisionCorrection(Asteroid *obj);
    virtual bool collisionCorrection(BlackholeObject *obj);
    virtual bool collisionCorrection(Meteor *obj);
    virtual bool collisionCorrection(PowerUp *obj);
    bool collisionCorrection(Bullet *obj);
    virtual bool collisionCorrection(Flare *obj);
    virtual bool collisionCorrection(UserObj *obj);
    virtual bool collisionCorrection(Enemy *obj);
};
