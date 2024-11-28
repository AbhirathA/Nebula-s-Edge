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
// #include "Flare.h"
// #include "ObjectLauncher.h"
// #include "PowerUp.h"

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

	std::vector<Obj *> objList = {}; // list of created objects (make to map)
	std::map<int, Obj *> objMap = {};
	std::map<int, UserObj *> playerMap = {};
	AABBtree tree;
	// ObjectLauncher launcher;
	// std::map<PowerUp*, Obj*> activePowerUps;

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

	std::map<int, std::pair<int, int>> display();

	// int drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass); // add an object
	// int drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass); // add an object

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
		this->playerMap[id]->turnLeft(30);
	}
	void right(int id)
	{
		this->playerMap[id]->turnRight(30);
	}

	~Manager()
	{
		for (auto i : objMap)
		{
			delete i.second;
		}
	}
	void removeDead(std::vector<int> ids);

	int shoot(int id, int innerRadius, int outerRadius, int mass);
	int getHealth(int id)
	{
		return this->playerMap[id]->getHealth();
	}

	int getPoints(int id)
	{
		return this->playerMap[id]->getPoints();
	}

	int dropAsteroid(int x, int y, int innerRad, int outerRad, int mass);
	int dropBlackHole(int x, int y, int innerRad, int outerRad, int mass);
	int dropEnemy(int x, int y, int v, int res, int innerRad, int startSign, int outerRad, bool startX, int mass, int aim);
	int dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);
	int dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass, int health, int bulletSpeed, int bulletLife);
};

// void activatePowerUp(PowerUp* powerUp, Obj* target);
// void updatePowerUps();

// void Manager::activatePowerUp(PowerUp* powerUp, Obj* target) {
// powerUp->applyEffect(target);
// activePowerUps[powerUp] = target; }

// just call updatePowerUps in update once onde

// void Manager::updatePowerUps()
// {
// 	std::vector<PowerUp *> expiredPowerUps;

// 	for (auto &[powerUp, target] : activePowerUps)
// 	{
// 		powerUp->updateTime();
// 		if (powerUp->isExpired())
// 		{
// 			powerUp->revokeEffect(target);
// 			expiredPowerUps.push_back(powerUp);
// 		}
// 	}

// 	// Remove expired power-ups
// 	for (PowerUp *powerUp : expiredPowerUps)
// 	{
// 		activePowerUps.erase(powerUp);
// 		delete powerUp; // Clean up the power-up object
// 	}
// }
