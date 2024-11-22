#pragma once
#include "LinearObj.h"
class velVerlet : public LinearObj{
    public:
        velVerlet(int id, int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass) : LinearObj(id, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass) {}
        virtual void updatePos(int t) override final;
        virtual int getNextX(int t) override final;
        virtual int getNextY(int t) override final;

        virtual bool checkCollision(LinearObj* obj) override final;
        bool checkCollision(Obj* obj) { return false; }
        virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);
        virtual bool collisionCorection(LinearObj* other);
        virtual bool collisionCorection(Obj* other) { return false; }

        virtual ~velVerlet() {}
};

