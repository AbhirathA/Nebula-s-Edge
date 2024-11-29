#pragma once
#include "FixedObj.h"

class PowerUp : public FixedObj
{
protected:
    int duration;
    int remainingTime;

public:
    PowerUp(int id, int x, int y, int radius, int duration):FixedObj(id, x, y, radius, radius, 0), duration(duration), remainingTime(duration) {}

    void setID(int id){
        this->id = id;
    }

    // look into what type of object goes here
    virtual void applyEffect(LinearObj *target) = 0;
    virtual void revokeEffect(LinearObj *target) = 0;

    void updateTime()
    {
        if (remainingTime > 0)
        {
            remainingTime--;
        }
    }

    bool isExpired() const
    {
        return remainingTime <= 0;
    }

    virtual ~PowerUp() {}
};

class IncreaseMassPowerUp : public PowerUp
{
private:
    int massIncrease;

public:
    IncreaseMassPowerUp(int id, int x, int y, int radius, int massIncrease, int duration)
        : PowerUp(id, x, y, radius, duration), massIncrease(massIncrease) {}

    void applyEffect(LinearObj *target) override
    {
        target->updateMass(target->getMass() + massIncrease);
    }

    void revokeEffect(LinearObj *target) override
    {
        target->updateMass(target->getMass() - massIncrease);
    }
};

class SpeedBoostPowerUp : public PowerUp
{
private:
    int boostX, boostY;

public:
    SpeedBoostPowerUp(int id, int x, int y, int radius, int boostX, int boostY, int duration)
        : PowerUp(id, x, y, radius, duration), boostX(boostX), boostY(boostY) {}

    void applyEffect(LinearObj *target) override
    {
        target->updateV(target->getvX() + boostX, target->getvY() + boostY);
    }

    void revokeEffect(LinearObj *target) override
    {
        target->updateV(target->getvX() - boostX, target->getvY() - boostY);
    }
};