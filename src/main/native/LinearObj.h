#pragma once
#include "Obj.h"
class LinearObj : public Obj
{

protected:
	// The velocity of the object
	double vX = 0;
	double vY = 0;
	double terminalX = 200;
	double terminalY = 200;
	bool isXTerminal = false;
	bool isYTerminal = false;

	// The acceleration of the object
	double accX = 0;
	double accY = 0;

public:
	LinearObj(int id, double x, double y, double vX, double vY, double accX, double accY, double res, double innerRad, double outerRad, double mass) : Obj(id, x, y, res, innerRad, outerRad, mass)
	{
		this->vX = vX;
		this->vY = vY;
		this->accX = accX;
		this->accY = accY;
	}

	double getvX()
	{
		return vX;
	}

	double getvY()
	{
		return vY;
	}

	virtual void updateV(double x, double y)
	{
		vX = x;
		vY = y;
	}

	virtual double getXacc()
	{
		return accX;
	}

	virtual double getYacc()
	{
		return accY;
	}

	virtual void updateAcc(double x, double y)
	{
		accX = x;
		accY = y;
	}

	// Sin of angle wrt x-axis
	double getOri()
	{
		return ((double)vX) / std::sqrt(vX * vX + vY * vY);
	}

	bool checkXTerminal()
	{
		return isXTerminal;
	}

	bool checkYTerminal()
	{
		return isYTerminal;
	}

	virtual bool checkCollision(Obj *obj) = 0;
	virtual void updatePos(double t) = 0;
	// void doubleernalUpdate();
	virtual double getNextX(double t) = 0;
	virtual double getNextY(double t) = 0;
	virtual bool boundCorrection(double lft, double rt, double tp, double bt, double t) = 0;
	virtual bool collisionCorection(Obj *other) = 0;
	virtual ~LinearObj() {};
};
