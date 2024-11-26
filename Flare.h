#pragma once
#include "FixedObj.h"
#include <string>

class Flare : public FixedObj
{
private:
    int duration;
    int remainingTime;
    std::string effectType;

public:
    Flare(int id, int x, int y, int radius, int duration, const std::string &effectType)
        : FixedObj(id, x, y, radius, radius, 0), duration(duration), remainingTime(duration), effectType(effectType) {}

    void update()
    {
        if (remainingTime > 0)
        {
            remainingTime--;
        }
        else
        {
            selfDestruct();
        }
    }

    bool isActive() const
    {
        return remainingTime > 0;
    }
};
