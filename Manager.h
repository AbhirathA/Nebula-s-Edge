#pragma once
#include "Obj.h"
#include "stdVerlet.h"
#include "velVerlet.h"
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
		int drop1(int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass); // add an object
		int drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass); // add an object
		void update();
		int xForce();
		int yForce();
		~Manager() {
			for (auto i : objList) {
				delete i;
			}
		}
};

