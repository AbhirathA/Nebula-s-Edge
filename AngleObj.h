#pragma once
#include "Obj.h"
#include <cmath>

class AngleObj:Obj{
	
	protected:
		// ID of the obj
		int id = 0;

		// Orientation
		double angle = 0;

		//The X-Y coordinates of the object
		double posX = 0;
		double posY = 0;

		//The velocity of the object
		double v = 0;

		//The acceleration of the object
		double acc = 0;
		double accX = 0;
		double accY = 0;

		//The state of the object
		int state = 0;

		//The mass and radii of the object
		double mass = 0;
		double innerRad = 0;
		double outerRad = 0;

	public:

		AngleObj(int id, double x, double y, double v, double angle, double acc, double accX, double accY, double innerRad, double outerRad, double mass) {
			this->id = id;
			this->posX = x;
			this->posY = y;
			this->v = v;
			this->angle = angle;
			this->acc = acc;
			this->accX = accX;
			this->accY = accY;
			this->innerRad = innerRad;
			this->outerRad = outerRad;
			this->mass = mass;
		}

		double getX() {
			return posX;
		}

		double getY() {
			return posY;
		}

		virtual double getV() {
			return this->v;
		}

		virtual void updateV(double v) {
			this->v = v;
		}

		virtual void updateX(double x) {
			posX = x;
		}

		virtual void updateY(double y) {
			posY = y;
		}

		virtual double getXacc() {
			return accX;
		}

		virtual double getYacc() {
			return accY;
		}

		virtual void updateAcc(double x, double y) {
			accX = x; accY = y;
		}

		double getAngle() {
			return this->angle;
		}

		// Sin of angle wrt x-axis
		double getOri() {
			return std::sin(this->angle);
		}

		double getOuterR() {
			return outerRad;
		}

		double getInnerR() {
			return innerRad;
		}

		int getState() {
			return state;
		}

		double getMass() {
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

