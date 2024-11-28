//
// Created by gathik on 26/11/24.
//
#include "FixedObj.h"

#pragma once

class Asteroid: public FixedObj {
    public:
    Asteroid(int id, int x, int y, int innerRad, int outerRad, int mass):FixedObj(id, x, y, innerRad, outerRad, mass) {}

    virtual bool checkCollision(Obj* obj) override {
            return obj->checkCollision(this);
        }
    virtual bool checkCollision(Asteroid *obj) override;
    virtual bool checkCollision(BlackholeObject * obj) override;
    virtual bool checkCollision(Meteor* obj) override;
    virtual bool checkCollision(Flare* obj) override;
    virtual bool checkCollision(PowerUp* obj) override;
    virtual bool checkCollision(UserObj* obj) override;
    virtual bool checkCollision(Enemy* obj) override;
    virtual bool checkCollision(Bullet* obj) override;

    virtual bool collisionCorrection(Obj* obj) override{
        return obj->collisionCorrection(this);
    }
    virtual bool collisionCorrection(Asteroid* obj) override;
    virtual bool collisionCorrection(BlackholeObject* obj) override;
    virtual bool collisionCorrection(Meteor* obj) override;
    virtual bool collisionCorrection(PowerUp* obj) override;
    virtual bool collisionCorrection(Bullet* obj) override;
    virtual bool collisionCorrection(Flare* obj) override;
    virtual bool collisionCorrection(UserObj* obj) override;
    virtual bool collisionCorrection(Enemy* obj) override;

};



