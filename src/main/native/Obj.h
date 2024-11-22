// Obj.h
#pragma once
#include <cmath>

// Different types of physics to be used for the simulation
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
	int posX = 0;
	int posY = 0;

	// The state of the object
	int state = 0;

	// The mass and radii of the object
	int mass = 0;
	int innerRad = 0;
	int outerRad = 0;

public:
	Obj(int id, int x, int y, int innerRad, int outerRad, int mass)
		: id(id), posX(x), posY(y), innerRad(innerRad), outerRad(outerRad), mass(mass) {}

	virtual ~Obj() {}

	int getX()
	{
		return posX;
	}

	int getY()
	{
		return posY;
	}

	virtual void updateX(int x)
	{
		posX = x;
	}

	virtual void updateY(int y)
	{
		posY = y;
	}

	int getOuterR()
	{
		return outerRad;
	}

	int getInnerR()
	{
		return innerRad;
	}

	int getState()
	{
		return state;
	}

	int getMass()
	{
		return mass;
	}

	void changeState()
	{
		state = 1 - state;
	}

	// Pure virtual functions
	virtual bool checkCollision(Obj *obj) = 0;
	virtual void updatePos(int t) = 0;
	virtual int getNextX(int t) = 0;
	virtual int getNextY(int t) = 0;
	virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t) = 0;
	virtual bool collisionCorection(Obj *other) = 0;
};
