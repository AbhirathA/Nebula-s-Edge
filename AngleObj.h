#pragma once
#include "Obj.h"
#include <cmath>
#define SCALE 1000

class AngleObj:Obj{
	
	protected:
		// Orientation
		int angleScaled = 0;

		//The velocity of the object
		int v = 0;

		//The acceleration of the object
		int acc = 0;
		int accX = 0;
		int accY = 0;

	public:

		AngleObj(int id, int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass):Obj(id,x,y,innerRad,outerRad, mass) {
			this->v = v;
			this->angleScaled = angle;
			this->acc = acc;
			this->accX = accX;
			this->accY = accY;
		}

		virtual int getV() {
			return this->v;
		}

		virtual void updateV(int v) {
			this->v = v;
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

		virtual void updateRadialAcc(int a) {
			this->acc = a;
		}

		int getAngle() {
			return this->angleScaled;
		}

		virtual bool checkCollision(Obj* obj) = 0;
		virtual void updatePos(int t) = 0;
		//void internalUpdate();

		virtual int getNextX(int t) = 0;
		virtual int getNextY(int t) = 0;

		virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t) = 0;
		virtual bool collisionCorection(Obj* other) = 0;
		virtual ~AngleObj() {};

};

