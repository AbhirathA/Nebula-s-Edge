#pragma once
#include "LinearObj.h"
class velVerlet :
    public LinearObj
{
    velVerlet(double id, double x, double y, double vX, double vY, double accX, double accY, double res, double innerRad, double outerRad, double mass) : LinearObj(id, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass) {}
    virtual void updatePos(double t) override final;
    virtual double getNextX(double t) override final;
    virtual double getNextY(double t) override final;

    virtual bool checkCollision(Obj* obj) override final;
    virtual bool boundCorrection(double lft, double rt, double tp, double bt, double t);
    virtual bool collisionCorection(LinearObj* other);

    virtual ~velVerlet() {}
};

