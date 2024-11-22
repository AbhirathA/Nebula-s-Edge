#pragma once
#include "LinearObj.h"
class Tracker : public LinearObj
{
    protected:
        Obj* target = nullptr;
        int velocity = 0;

    public:
        Tracker(int id, int x, int y, int v, int res, int innerRad, int outerRad, int mass, bool startX, int startSign, Obj* aim) : LinearObj(id, x, y, startX ? startSign * v : 0, startX ? 0 : startSign * v, 0, 0, res, innerRad, outerRad, mass), target{ aim }, velocity{v}{}
        virtual void updatePos(int t) override final;
        virtual int getNextX(int t) override final;
        virtual int getNextY(int t) override final;

        virtual bool checkCollision(Obj* obj);
        virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);
        virtual bool collisionCorection(Obj* other);

        virtual ~Tracker() {}
};

