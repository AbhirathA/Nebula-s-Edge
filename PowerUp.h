#pragma once
#include "FixedObj.h"

class PowerUp : public FixedObj
{
public:
    PowerUp(int id, int x, int y, int radius)
        : FixedObj(id, x, y, radius, radius, 0) {}

    void setID(int id)
    {
        this->id = id;
    }

    virtual void applyEffect(LinearObj *target) = 0;

    virtual ~PowerUp() {}
};

class IncreaseMassPowerUp : public PowerUp
{
private:
    int massIncrease;

public:
    IncreaseMassPowerUp(int id, int x, int y, int radius, int massIncrease)
        : PowerUp(id, x, y, radius), massIncrease(massIncrease) {}

    void applyEffect(LinearObj *target) override
    {
        target->updateMass(target->getMass() + massIncrease);
    }
};

class SpeedBoostPowerUp : public PowerUp
{
private:
    int boostX, boostY;

public:
    SpeedBoostPowerUp(int id, int x, int y, int radius, int boostX, int boostY)
        : PowerUp(id, x, y, radius), boostX(boostX), boostY(boostY) {}

    void applyEffect(LinearObj *target) override
    {
        target->updateV(target->getvX() + boostX, target->getvY() + boostY);
    }
};
