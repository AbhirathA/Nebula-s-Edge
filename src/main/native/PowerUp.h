#pragma once
#include "Lifetime.h"
#include "FixedObj.h"
#include "UserObj.h"

class PowerUp : public FixedObj
{
protected:
    int duration;
    int remainingTime;
    Lifetime *lifetime = nullptr;
    UserObj *target = nullptr;
    virtual void revokeEffect() = 0;

public:
    PowerUp(int id, int x, int y, int radius, int duration) : FixedObj(id, x, y, radius, radius, 0), duration(duration), remainingTime(duration)
    {
        lifetime = new Lifetime(duration, [this]() -> void
                                { this->revokeEffect(); });
    }

    // look into what type of object goes here
    virtual void applyEffect(UserObj *target) = 0;

    virtual ~PowerUp() { delete this->lifetime; }
};

class IncreaseHealthPowerUp : public PowerUp
{
private:
    int healthIncrease;

public:
    IncreaseHealthPowerUp(int id, int x, int y, int radius, int healthIncrease) : PowerUp(id, x, y, radius, 1), healthIncrease(healthIncrease) {}

    void applyEffect(UserObj *target) override
    {
        target->heal(healthIncrease);
        lifetime->start();
    }

    void revokeEffect() override
    {
        return;
    }

    virtual ~IncreaseHealthPowerUp() {}
};

class BulletBoostPowerUp : public PowerUp
{
private:
    int speedBoost, lifeBoost;

public:
    BulletBoostPowerUp(int id, int x, int y, int radius, int speedBoost, int lifeBoost, int duration) : PowerUp(id, x, y, radius, duration), speedBoost(speedBoost), lifeBoost(lifeBoost) {}

    void applyEffect(UserObj *target) override
    {
        target->incBulletSpeed(speedBoost);
        target->incBulletLife(lifeBoost);
        this->target = target;
        lifetime->start();
    }

    void revokeEffect() override
    {
        if (this->target == nullptr)
            return;
        target->decBulletSpeed(speedBoost);
        target->decBulletLife(lifeBoost);
    }
};

class IncreasePointsPowerUp : public PowerUp
{
private:
    int pointScale;

public:
    IncreasePointsPowerUp(int id, int x, int y, int radius, int pointScale, int duration) : PowerUp(id, x, y, radius, duration), pointScale(pointScale) {}

    void applyEffect(UserObj *target) override
    {
        target->scaleUpPoints(pointScale);
        this->target = target;
        lifetime->start();
    }

    void revokeEffect() override
    {
        if (this->target == nullptr)
            return;
        target->scaleDownPoints(pointScale);
    }

    virtual ~IncreasePointsPowerUp() {}
};