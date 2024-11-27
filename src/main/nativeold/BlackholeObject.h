#pragma once
#include <vector>
#include <set>
#include <cmath>
#include "FixedObj.h"
#include <set>

class BlackholeObject : public FixedObj
{
private:
    int innerRadius;
    static constexpr int G = 10000;
    static constexpr int FLOATING_ERROR = 1;
    static constexpr int GROWTH_FACTOR = 1; // fidn a good value maybe 0.0001
    std::set<FixedObj *> objects;

public:
    BlackholeObject(int id, int x, int y, int innerRad, int outerRad, int mass) : FixedObj(id, x, y, innerRad, outerRad, mass), innerRadius(innerRad) {
                                                                                  };

    void addObject(FixedObj *obj)
    {
        objects.insert(obj);
    }
    void absorb(FixedObj *obj)
    {
        this->mass += obj->getMass();
        this->outerRad += obj->getMass() * GROWTH_FACTOR;
        this->updateBox();
    }
    void interactWith(FixedObj *obj)
    {
        if (dynamic_cast<BlackholeObject *>(obj))
            return;

        int dx = this->getX() - obj->getX();
        int dy = this->getY() - obj->getY();
        int distanceSquared = dx * dx + dy * dy;

        if (distanceSquared < (this->innerRadius + obj->getInnerR()) * (this->innerRadius + obj->getInnerR()))
        {
            obj->selfDestruct();
            this->absorb(obj);
        }
        else if (distanceSquared < (this->outerRad + obj->getOuterR()) * (this->outerRad + obj->getOuterR()))
        {
            applyGravitationalForce(obj, dx, dy, distanceSquared);
        }
    }
    void applyGravitationalForce(FixedObj *obj, int dx, int dy, int distanceSquared)
    {
        double distance = std::sqrt(static_cast<double>(distanceSquared));
        int force = static_cast<int>(G * this->getMass() * obj->getMass() / distanceSquared);

        int accX = static_cast<int>(force * (dx / distance));
        int accY = static_cast<int>(force * (dy / distance));

        obj->updateV(accX, accY);
    }

    void update()
    {
        std::vector<FixedObj *> toRemove;
        for (FixedObj *obj : objects)
        {
            interactWith(obj);

            if (*obj->getStatus())
            {
                toRemove.push_back(obj);
            }
        }
        for (FixedObj *obj : toRemove)
        {
            objects.erase(obj);
        }
    }
};
