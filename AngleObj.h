#pragma once
#include "Obj.h"
#include <cmath>
#define SCALE 1000

class AngleObj:Obj{
	
	protected:
		// ID of the obj
		int id = 0;

		// Orientation
		int angleScaled = 0;

		//The X-Y coordinates of the object
		int posX = 0;
		int posY = 0;

		//The velocity of the object
		int v = 0;

		//The acceleration of the object
		int acc = 0;
		int accX = 0;
		int accY = 0;

		//The state of the object
		int state = 0;

		//The mass and radii of the object
		int mass = 0;
		int innerRad = 0;
		int outerRad = 0;

	public:

		AngleObj(int id, int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass):Obj(id,x,y,innerRad,outerRad, mass) {
			this->id = id;
			this->posX = x;
			this->posY = y;
			this->v = v;
			this->angleScaled = angle;
			this->acc = acc;
			this->accX = accX;
			this->accY = accY;
			this->innerRad = innerRad;
			this->outerRad = outerRad;
			this->mass = mass;
		}

		int getX() {
			return posX;
		}

		int getY() {
			return posY;
		}

		virtual int getV() {
			return this->v;
		}

		virtual void updateV(int v) {
			this->v = v;
		}

		virtual void updateX(int x) {
			posX = x;
		}

		virtual void updateY(int y) {
			posY = y;
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

		int getAngle() {
			return this->angleScaled;
		}

		// Sin of angle wrt x-axis
		double getOri() {
			return std::sin(this->angleScaled/SCALE);
		}

		int getOuterR() {
			return outerRad;
		}

		int getInnerR() {
			return innerRad;
		}

		int getState() {
			return state;
		}

		int getMass() {
			return mass;
		}

		void changeState() {
			state = 1 - state;
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

