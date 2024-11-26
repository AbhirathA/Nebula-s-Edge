#pragma once
#include <vector>
#include <set>
#include <cmath>
#include "FixedObj.h"
#include <set>

class Blackhole : public FixedObj
{
private:
    int innerRadius;
    static constexpr int G = 10000;
    static constexpr int FLOATING_ERROR = 1;
    std::set<FixedObj *> objects;

public:
    Blackhole(int id, int x, int y, int innerRad, int outerRad, int mass) : FixedObj(id, x, y, innerRad, outerRad, mass), innerRadius(innerRad) {};

    void addObject(FixedObj *obj)
    {
        objects.insert(obj);
    }
    void update()
    {
        std::vector<FixedObj *> toRemove;
        for (FixedObj *obj : objects)
        {
            if (dynamic_cast<Blackhole *>(obj))
            {
                continue;
            }

            int dx = this->getX() - obj->getX();
            int dy = this->getY() - obj->getY();

            int distanceSquared = dx * dx + dy * dy;
            if (distanceSquared < (innerRad + obj->getInnerR()) * (innerRad + obj->getInnerR()) - FLOATING_ERROR)
            {
                obj->selfDestruct();
                toRemove.push_back(obj);
            }
            else if (distanceSquared < (this->getOuterR() + obj->getOuterR()) * (this->getOuterR() + obj->getOuterR()) - FLOATING_ERROR)
            {
                double distance = std::sqrt(static_cast<double>(distanceSquared));
                int force = static_cast<int>(G * this->getMass() * obj->getMass() / distanceSquared);

                int accX = static_cast<int>(force * (dx / distance));
                int accY = static_cast<int>(force * (dy / distance));

                obj->updateAcc(accX, accY);
            }
            else
            {
                toRemove.push_back(obj);
            }
        }
    }
};
