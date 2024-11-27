#pragma once
#include "LinearObj.h"
#include "AngleObj.h"

class velVerlet : public LinearObj
{
public:
    velVerlet(int id, int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass) : LinearObj(id, x, y, vX, vY, accX, accY, innerRad, outerRad, mass) {}
    virtual void updatePos(int t) override final;
    virtual int getNextX(int t) override final;
    virtual int getNextY(int t) override final;

    virtual bool checkCollision(LinearObj *obj);
    virtual bool checkCollision(AngleObj *obj) { return obj->checkCollision(this); }
    virtual bool checkCollision(Obj *obj) { return obj->checkCollision(this); }

    virtual bool collisionCorrection(LinearObj *other);
    virtual bool collisionCorrection(AngleObj *other) { return other->collisionCorrection(this); }
    virtual bool collisionCorrection(Obj *other) { return other->collisionCorrection(this); }

    virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);

    virtual ~velVerlet() {}
};
