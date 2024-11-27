#pragma once
#include "Obj.h"
#include "stdVerlet.h"
#include "velVerlet.h"
#include "CtrledObj.h"
#include <vector>
#include<map>
#include <iostream>
#define PRECISION 1

class Manager
{
	// Unique id creation
	int count = 0;


	int gX = 0; // wind speed
	int gY = -1; // gravity
	int lft = 0; // left boundary
	int rt = 1000; // right boundary
	int tp = 0; // top boundary
	int bt = -1000; // bottom boundary
	int t = 1; // time scale
	int precision = 2; // precision of overlap resolution

	CtrledObj* player = nullptr;

	std::vector<Obj*> objList = {}; // list of created objects (make to map)
	std::map<int, Obj*> objMap = {};
	public:
		
		Manager(int accX = 0, int accY = 2, int lft = 0, int rt = 1000, int tp = 0, int bt = -1000, int t = 1) {
			this->gX = accX;
			this->gY = accY;
			this->lft = lft;
			this->rt = rt;
			this->tp = tp;
			this->bt = bt;
			this->t = t;
		}

		std::map<int, std::pair<int, int>>  display();
		int dropP(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass);
		int drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass); // add an object
		int drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass); // add an object
		void update();
		void forward() {
			this->player->moveForward();
		}
		void stop() {
			this->player->stopForward();
		}
		void thrust() {
			this->player->startThrust();
		}
		void left() {
			this->player->turnLeft(5);
		}
		void right() {
			this->player->turnRight(5);
		}

		double angle() {
			return this->player->getAngle();
		}

		int xForce();
		int yForce();
		~Manager() {
			for (auto i : objList) {
				delete i;
			}
		}
};

