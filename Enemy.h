//
// Created by ibrahim on 27/11/24.
//

#ifndef ENEMY_H
#define ENEMY_H
#include "Tracker.h"


class Enemy : public Tracker{
public:
    Enemy(int id, int x, int y, int v, int res, int innerRad, int outerRad, int mass, bool startX, int startSign,
        Obj *aim)
        : Tracker(id, x, y, v, res, innerRad, outerRad, mass, startX, startSign, aim) {
    }
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



#endif //ENEMY_H
