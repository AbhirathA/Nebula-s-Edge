//
// Created by ibrahim on 27/11/24.
//

#pragma once

#include "velVerlet.h"
#include "Lifetime.h"
#include "Asteroid.h"
#include "BlackholeObject.h"
#include "Meteor.h"
#include "Flare.h"
#include "PowerUp.h"
#include "UserObj.h"
#include "Enemy.h"

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
    virtual bool checkCollision(Meteor *obj) override;
    virtual bool checkCollision(Flare *obj);
    virtual bool checkCollision(PowerUp *obj);
    virtual bool checkCollision(UserObj *obj);
    virtual bool checkCollision(Enemy *obj) override;
    bool checkCollision(Bullet *obj) override;

    bool collisionCorrection(Obj *obj)
    {
        return obj->collisionCorrection(this);
    }

    virtual bool collisionCorrection(Asteroid *obj) override;
    virtual bool collisionCorrection(BlackholeObject *obj) override;
    virtual bool collisionCorrection(Meteor *obj) override;
    virtual bool collisionCorrection(PowerUp *obj) override;
    bool collisionCorrection(Bullet *obj) override;
    virtual bool collisionCorrection(Flare *obj) override;
    virtual bool collisionCorrection(UserObj *obj) override;
    virtual bool collisionCorrection(Enemy *obj) override;
};
