#pragma once
#include <vector>
#include "Flare.h"
#include "PowerUp.h"
#include "stdVerlet.h"
#include "velVerlet.h"

class ObjectLauncher
{
private:
    std::vector<Obj *> launchedObjects;
    int objectCounter = 0;

public:
    Flare *launchFlare(int x, int y, int vX, int vY, int accX, int accY, int radius, int mass, int duration)
    {
        Flare *flare = new Flare(objectCounter++, x, y, vX, vY, accX, accY, radius, radius, mass, duration);
        launchedObjects.push_back(flare);
        return flare;
    }

    stdVerlet *launchProjectile(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass)
    {
        stdVerlet *projectile = new stdVerlet(objectCounter++, x, y, vX, vY, accX, accY, 1, innerRad, outerRad, mass);
        launchedObjects.push_back(projectile);
        return projectile;
    }

    PowerUp *launchPowerUp(PowerUp *powerUp)
    {
        powerUp->setID(objectCounter++);
        launchedObjects.push_back(powerUp);
        return powerUp;
    }

    void updateObjects(int t)
    {
        for (auto it = launchedObjects.begin(); it != launchedObjects.end();)
        {
            (*it)->updatePos(t);
            if (*((*it)->getStatus()))
            {
                delete *it;
                it = launchedObjects.erase(it);
            }
            else
            {
                ++it;
            }
        }
    }

    ~ObjectLauncher()
    {
        for (Obj *obj : launchedObjects)
        {
            delete obj;
        }
    }
};
