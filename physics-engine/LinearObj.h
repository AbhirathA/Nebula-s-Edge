#pragma once
#include "Obj.h"
#include <iostream>
class LinearObj : public Obj {

	protected:
		//The velocity of the object in x-y form
		int vX = 0;
		int vY = 0;
		int terminalX = 100;
		int terminalY = 100;
		bool isXTerminal = false;
		bool isYTerminal = false;

		//The acceleration of the object
		int accX = 0;
		int accY = 0;

	public:

		LinearObj(int id, int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass) :Obj(id, x, y, innerRad, outerRad, mass) {
			this->vX = vX;
			this->vY = vY;
			this->accX = accX;
			this->accY = accY;
			std::cout << "Constructed LO" << x <<" " << y<< std::endl;
		}

		int getvX() {
			return vX;
		}

		int getvY() {
			return vY;
		}

		virtual void updateV(int x, int y) {
			vX = x; vY = y;
		}

		virtual int getXacc() {
			return accX;
		}

		virtual int getYacc() {
			return accY;
		}

		virtual void updateAcc(int x, int y) {
			accX = x; accY = y;
		}

		// Sin of angle wrt x-axis
		double getOri() {
			return ((double)vX) / std::sqrt(vX * vX + vY * vY);
		}

		bool checkXTerminal() {
			return isXTerminal;
		}

		bool checkYTerminal() {
			return isYTerminal;
		}

		virtual bool checkCollision(Obj* obj) = 0;
		virtual bool checkCollision(LinearObj* lo) = 0;
		virtual bool checkCollision(AngleObj* ao) = 0;

		virtual bool collisionCorrection(Obj* other) = 0;
		virtual bool collisionCorrection(LinearObj* other) = 0;
		virtual bool collisionCorrection(AngleObj* other) = 0;

		virtual void updatePos(int t) = 0;

		virtual int getNextX(int t) = 0;
		virtual int getNextY(int t) = 0;
		virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t) = 0;


		virtual ~LinearObj() {};
};

