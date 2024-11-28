#pragma once
#include "Obj.h"
#include "stdVerlet.h"
#include "velVerlet.h"
#include "CtrledObj.h"
#include <vector>
#include <map>
#include "Lifetime.h"
#include <tuple>
#include <iostream>

#include "UserObj.h"

#define PRECISION 1

class Manager
{
	// Unique id creation
	int count = 0;

	int gX = 0;		   // wind speed
	int gY = -1;	   // gravity
	int lft = 0;	   // left boundary
	int rt = 1000;	   // right boundary
	int tp = 0;		   // top boundary
	int bt = -1000;	   // bottom boundary
	int t = 1;		   // time scale
	int precision = 2; // precision of overlap resolution

	std::vector<Obj *> objList = {};
	std::map<int, Obj *> objMap = {};
	std::map<int, UserObj *> playerMap = {};
	AABBtree tree;

public:
	Manager(int accX = 0, int accY = 2, int lft = 0, int rt = 1000, int tp = 0, int bt = -1000, int t = 1)
	{
		this->gX = accX;
		this->gY = accY;
		this->lft = lft;
		this->rt = rt;
		this->tp = tp;
		this->bt = bt;
		this->t = t;
		tree = AABBtree();
	}

	int dropP(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass);
	int drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass); // add an object
	int drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);			  // add an object

	void update();
	std::vector<std::vector<int>> display(int lowerX, int lowerY, int upperX, int upperY);

	void forward(int id)
	{
		this->playerMap[id]->moveForward();
	}
	void stop(int id)
	{
		this->playerMap[id]->stopForward();
	}
	void thrust(int id)
	{
		this->playerMap[id]->startThrust();
	}
	void left(int id)
	{
		this->playerMap[id]->turnLeft(5);
	}
	void right(int id)
	{
		this->playerMap[id]->turnRight(5);
	}

	int xForce();
	int yForce();
	~Manager()
	{
		for (auto i : objMap)
		{
			delete i.second;
		}
	}
	void removeDead(std::vector<int> ids);

	int shoot(int id, int innerRadius, int outerRadius, int mass);

	int dropAsteroid(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);
	int dropBlackHole(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);
	int dropEnemy(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);
	int dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);
	int dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass);
};
