#pragma once
#include "Obj.h"
#include "LinearObj.h"
#include <cmath>
#define VALUE_SCALE 1000
#define ANGLE_SCALE 10

class AngleObj : public Obj
{

private:
	int mod(int a, int b)
	{
		while (a > b)
		{
			a -= b;
		}
		while (a < 0)
		{
			a += b;
		}
		return a;
	}

protected:
	// Angle Computation Pre-requisites
	static int SIN[3600];
	static int COS[3600];
	static double PI;
	static bool initialized;

	// Orientation
	int angleScaled = 0;

	// The velocity of the object
	int v = 0;

	// The acceleration of the object
	int acc = 0;
	int accX = 0;
	int accY = 0;

public:
	AngleObj(int id, int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass) : Obj(id, x, y, innerRad, outerRad, mass)
	{
		this->v = v;
		this->angleScaled = (angle * ANGLE_SCALE) % (360 * ANGLE_SCALE);
		this->acc = acc;
		this->accX = accX;
		this->accY = accY;

		std::cout << "Created AO: " << v << " " << angle << std::endl;
		this->initializeTrig();
	}

	virtual int getvX()
	{
		return this->v * COS[this->angleScaled];
	}

	virtual int getvY()
	{
		return this->v * SIN[this->angleScaled];
	}

	virtual int getaccX()
	{
		return this->acc * COS[this->angleScaled] + this->accX * VALUE_SCALE;
	}

	virtual int getaccY()
	{
		return this->acc * SIN[this->angleScaled] + this->accY * VALUE_SCALE;
	}

	virtual int getV()
	{
		return this->v;
	}

	virtual void updateV(int v)
	{
		this->v = v;
	}

	virtual void updateV(int vX, int vY, int scale);

	virtual int getXacc()
	{
		return accX;
	}

	virtual int getYacc()
	{
		return accY;
	}

	virtual void updateAcc(int x, int y)
	{
		accX = x;
		accY = y;
	}

	virtual void updateRadialAcc(int a)
	{
		this->acc = a;
	}

	double getAngle()
	{
		return ((double)this->angleScaled) / ANGLE_SCALE;
	}

	virtual bool checkCollision(Obj *obj);
	virtual bool checkCollision(LinearObj *lo);
	virtual bool checkCollision(AngleObj *ao);

	virtual bool collisionCorrection(Obj *other);
	virtual bool collisionCorrection(LinearObj *other);
	virtual bool collisionCorrection(AngleObj *other);

	virtual void updatePos(int t);
	virtual int getNextX(int t);
	virtual int getNextY(int t);

	virtual void initializeTrig();

	virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);
	virtual ~AngleObj() {};
};