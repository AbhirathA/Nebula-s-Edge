#pragma once
#include <vector>
#include "FixedObj.h"
#include "Flare.h"

class ObjectLauncher
{
private:
    std::vector<FixedObj *> launchedObjects;
    int objectCounter = 0;

public:
    FixedObj *launchProjectile(int x, int y, int vx, int vy, int radius, int mass)
    {
        FixedObj *projectile = new FixedObj(objectCounter++, x, y, radius, radius, mass);
        projectile->updateV(vx, vy);
        launchedObjects.push_back(projectile);
        return projectile;
    }

    Flare *launchFlare(int x, int y, int radius, int duration, const std::string &effectType)
    {
        Flare *flare = new Flare(objectCounter++, x, y, radius, duration, effectType);
        launchedObjects.push_back(flare);
        return flare;
    }

    // Update all launched objects
    void updateObjects()
    {
        for (auto it = launchedObjects.begin(); it != launchedObjects.end();)
        {
            (*it)->update();
            if (*((*it)->getStatus()))
            {
                delete *it; // Clean up destroyed objects
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
        for (FixedObj *obj : launchedObjects)
        {
            delete obj;
        }
    }
};
