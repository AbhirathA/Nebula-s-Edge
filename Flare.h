#pragma once
#include "stdVerlet.h"

class Flare : public stdVerlet
{
private:
    int duration;
    int remainingTime;

public:
    Flare(int id, int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass, int duration)
        : stdVerlet(id, x, y, vX, vY, accX, accY, 1, innerRad, outerRad, mass), duration(duration), remainingTime(duration) {}

    void updateFlare(int t)
    {
        stdVerlet::updatePos(t);

        if (remainingTime > 0)
        {
            remainingTime--;
        }
        else
        {
            this->selfDestruct();
        }
    }

    bool checkCollision(LinearObj *obj) override
    {
        return stdVerlet::checkCollision(obj);
    }
};
