#pragma once
#include <cmath>

// Different the type of physics to be used for the simulation
enum class TypeOfPhy
{
	FIXEDOBJ = 0,
	MOVINGOBJ = 1,
	ACCTRACKER = 2,
	TRACKER = 3,
	BEEMAN = 4,
	STDVERLET = 5,
	VELVERLET = 6,
	Other = 7
};

/*
	This class contains all the basic properties every object should have.
	It also looks at basic functionality that every object should have.
*/

class Obj
{
protected:
	int id = 0;

	// The X-Y coordinates of the object
	double posX = 0;
	double posY = 0;

	// The state of the object
	double state = 0;

	// The mass and radii of the object
	double mass = 0;
	double innerRad = 0;
	double outerRad = 0;

	double res = 0;

public:
	Obj(int id, double x, double y, double res, double innerRad, double outerRad, double mass)
	{
		this->id = id;
		this->posX = x;
		this->posY = y;
		this->innerRad = innerRad;
		this->outerRad = outerRad;
		this->mass = mass;
		this->res = res;
	}

	double getX()
	{
		return posX;
	}

	double getY()
	{
		return posY;
	}

	virtual void updateX(double x)
	{
		posX = x;
	}

	virtual void updateY(double y)
	{
		posY = y;
	}

	double getOuterR()
	{
		return outerRad;
	}

	double getInnerR()
	{
		return innerRad;
	}

	double getState()
	{
		return state;
	}

	double getMass()
	{
		return mass;
	}

	void changeState()
	{
		state = 1 - state;
	}

	virtual bool checkCollision(Obj *obj) = 0;
	virtual void updatePos(double t) = 0;
	// void internalUpdate();
	virtual double getNextX(double t) = 0;
	virtual double getNextY(double t) = 0;
	virtual bool boundCorrection(double lft, double rt, double tp, double bt, double t) = 0;
	virtual bool collisionCorection(Obj *other) = 0;
	virtual ~Obj() {};
};