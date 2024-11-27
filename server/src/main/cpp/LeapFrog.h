#pragma once
#include "LinearObj.h"
class LeapFrog :
    public LinearObj
{
    LeapFrog(int id, int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass) : LinearObj(id, x, y, vX, vY, accX, accY, innerRad, outerRad, mass) {}
    virtual void updatePos(int t) override final;
    virtual int getNextX(int t) override final;
    virtual int getNextY(int t) override final;

    virtual bool checkCollision(Obj* obj) override final;
    virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);
    virtual bool collisionCorection(Obj* other);

    virtual ~LeapFrog() {}
};

