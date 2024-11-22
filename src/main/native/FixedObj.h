// FixedObj.h
#pragma once
#include "LinearObj.h"

class FixedObj : public LinearObj
{
public:
	FixedObj(int id, double x, double y, double innerRad, double outerRad, double mass);

	// Override methods from LinearObj and Obj
	virtual void updatePos(double t) override final;
	virtual double getNextX(double t) override final;
	virtual double getNextY(double t) override final;
	virtual bool checkCollision(Obj *obj) override final;
	virtual bool boundCorrection(double lft, double rt, double tp, double bt, double t) override;
	virtual bool collisionCorection(Obj *other) override;
};
