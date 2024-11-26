#pragma once
#include <cmath>
#include "AABBtree.h"

// Different the type of physics to be used for the simulation
enum class TypeOfPhy{
	FIXEDOBJ = 0,
    MOVINGOBJ = 1,
    ACCTRACKER = 2,
    TRACKER = 3,
	BEEMAN = 4,
    STDVERLET = 5,
	VELVERLET = 6,
    Other = 7
};

class LinearObj;

/*
	This class contains all the basic properties every object should have.
	It also looks at basic functionality that every object should have.
*/

class Obj
{
	protected:
		int id = 0;

		//The X-Y coordinates of the object
		int posX = 0;
		int posY = 0;

		//The state of the object
		int state = 0;
		int stateCount = 2;

		//The mass and radii of the object
		int mass = 0;
		int innerRad = 0;
		int outerRad = 0;

	// The bounding box of the object
	AABB *objBox = nullptr;

	// Status of the object(Alive or dead)
	bool dead = false;

public:
	Obj(int id, int x, int y, int innerRad, int outerRad, int mass)
	{
		this->id = id;
		this->posX = x;
		this->posY = y;
		this->innerRad = innerRad;
		this->outerRad = outerRad;
		this->mass = mass;
		this->objBox = new AABB({x - outerRad, y - outerRad}, {x + outerRad, y + outerRad});
		this->dead = false;
	}

	int getID()
	{
		return id;
	}

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

	AABB *getObjBox()
	{
		return objBox;
	}

	bool *getStatus()
	{
		return &dead;
	}

	void selfDestruct()
	{
		dead = true;
	}

	void updateBox()
	{
		objBox->setLowerBound({posX - outerRad, posY - outerRad});
		objBox->setUpperBound({posX + outerRad, posY + outerRad});
	}

	void updateMass(int m)
	{
		mass = m;
	}

	void changeState()
	{
		state = (state+1)%stateCount;
	}

	virtual bool checkCollision(Obj *obj) = 0;
	virtual void updatePos(int t) = 0;
	// void internalUpdate();
	virtual int getNextX(int t) = 0;
	virtual int getNextY(int t) = 0;
	virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t) = 0;
	virtual bool collisionCorection(Obj *other) = 0;
	virtual ~Obj()
	{
		delete objBox;
	};
		// virtual bool checkCollision(Obj* obj) = 0;
		virtual bool checkCollision(LinearObj* lo) = 0;

		virtual bool collisionCorrection(Obj* other) = 0;
		virtual bool collisionCorrection(LinearObj* other) = 0;

		// virtual void updatePos(int t) = 0;
		virtual void updateAcc(int ax, int ay) = 0;
		// void internalUpdate();
		// virtual int getNextX(int t) = 0;
		// virtual int getNextY(int t) = 0;
		// virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t) = 0;

		// virtual ~Obj() {};
};