#pragma once
#include <vector>
#include <set>
#include <cmath>
#include <algorithm>
#include "FixedObj.h"
#include "Utilities.h"
#include "Obj.h"
#include <set>

class BlackholeObject : public FixedObj
{
private:
    int innerRadius;
    static constexpr int G = 100000;
    static constexpr int FLOATING_ERROR = 1;
    static constexpr int GROWTH_FACTOR = 1; // fidn a good value maybe 0.0001

public:
    BlackholeObject(int id, int x, int y, int innerRad, int outerRad, int mass) : FixedObj(id, x, y, innerRad, outerRad, mass), innerRadius(innerRad) {
                                                                                  };

    bool interactWith(Obj *obj)
    {
        int dx = this->getX() - obj->getX();
        int dy = this->getY() - obj->getY();
        int distanceSquared = dx * dx + dy * dy;

        if (distanceSquared < (this->innerRadius + obj->getInnerR()) * (this->innerRadius + obj->getInnerR()))
        {
            obj->selfDestruct();
            return true;
        }
        else if (distanceSquared < (this->outerRad + obj->getInnerR()) * (this->outerRad + obj->getInnerR()))
        {
            applyGravitationalForce(obj, dx, dy, distanceSquared);
            return true;
        }
        else
        {
            obj->updateAcc(0, 0);
            return false;
        }
    }
    void applyGravitationalForce(Obj *obj, int dx, int dy, int distanceSquared)
    {
        double distance = std::sqrt(static_cast<double>(distanceSquared));
        int force = static_cast<int>(G * obj->getMass() / distanceSquared);

        int accX = static_cast<int>(force * (dx / distance));
        int accY = static_cast<int>(force * (dy / distance));

        obj->updateAcc(accX, accY);
    }

    virtual bool checkCollision(Obj* obj) override {
        return obj->checkCollision(this);
    }
    virtual bool checkCollision(Asteroid *obj) override {
        return false;
    }
    virtual bool checkCollision(BlackholeObject * obj) override{
        return false;
    }
    virtual bool checkCollision(Meteor* obj) override{
        return false;
    }
    virtual bool checkCollision(Flare* obj) override{
        return false;
    }
    virtual bool checkCollision(PowerUp* obj) override{
        return false;
    }
    virtual bool checkCollision(UserObj* obj) override{
        return false;
    }
    virtual bool checkCollision(Enemy* obj) override{
        return false;
    }
    virtual bool checkCollision(Bullet* obj) override{
        return false;
    }


    virtual bool collisionCorrection(Obj* obj) override{
        return obj->collisionCorrection(this);
    }
    virtual bool collisionCorrection(Asteroid* obj) override{
        return false;
    }
    virtual bool collisionCorrection(BlackholeObject* obj) override{
        return false;
    }
    virtual bool collisionCorrection(Meteor *obj) override{
        return false;
    }
    virtual bool collisionCorrection(PowerUp* obj) override{
        return false;
    }
    virtual bool collisionCorrection(Bullet* obj) override{
        return false;
    }
    virtual bool collisionCorrection(Flare* obj) override{
        return false;
    }
    virtual bool collisionCorrection(UserObj* obj) override{
        return false;
    }
    virtual bool collisionCorrection(Enemy* obj) override{
        return false;
    }

};
