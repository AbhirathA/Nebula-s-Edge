#pragma once
#include "LinearObj.h"
class Tracker : public LinearObj
{
    protected:
        Obj* target = nullptr;
        double velocity = 0;

    public:
        Tracker(int id, double x, double y, double v, double res, double innerRad, double outerRad, double mass, bool startX, double startSign, Obj* aim) : LinearObj(id, x, y, startX ? startSign * v : 0, startX ? 0 : startSign * v, 0, 0, res, innerRad, outerRad, mass), target{ aim }, velocity{v}{}
        virtual void updatePos(double t) override final;
        virtual double getNextX(double t) override final;
        virtual double getNextY(double t) override final;

        virtual bool checkCollision(Obj* obj);
        virtual bool boundCorrection(double lft, double rt, double tp, double bt, double t);
        virtual bool collisionCorection(Obj* other);

        virtual ~Tracker() {}
};

