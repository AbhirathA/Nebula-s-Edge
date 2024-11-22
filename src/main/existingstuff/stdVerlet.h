#pragma once
#include "LinearObj.h"
class stdVerlet : public LinearObj{

    protected:
    	double posX_vOld = 0;
		double posY_vOld = 0;
		double posX_old = 0;
		double posY_old = 0;

    public:
        stdVerlet(int id, double x, double y, double vX, double vY, double accX, double accY, double res, double innerRad, double outerRad, double mass) : LinearObj(id, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass) {
            
            this->posX_old = x - vX * res;
			this->posY_old = y - vY * res;
			this->posX_vOld = x - 2 * vX * res ; ///
			this->posY_vOld = y - 2 * vY * res ; ///
        }

        double getX_Old(){
            return posX_old;
        }

		double getY_Old(){
            return posY_old;
        }

		double getX_vOld(){
			return posX_vOld;
		}

		double getY_vOld(){
            return posY_vOld;
        }

		void updateX(double x, double o, double vo) {
			posX_vOld = vo;
			posX_old = o;
			posX = x;
		}

		void updateX(double x, double o) {
			posX_vOld = posX_old;
			posX_old = o;
			posX = x;
		}

		void updateX(double x) {
			posX_vOld = posX_old;
			posX_old = posX;
			posX = x;
		}

		void updateY(double y, double o, double vo) {
			posY_vOld = vo;
			posY_old = o;
			posY = y;
		}

		void updateY(double y, double o) {
			posY_vOld = posY_old;
			posY_old = o;
			posY = y;
		}

		void updateY(double y) {
			posY_vOld = posY_old;
			posY_old = posY;
			posY = y;
		}

        virtual void updatePos(double t) override final;
        virtual double getNextX(double t) override final;
        virtual double getNextY(double t) override final;

        virtual bool checkCollision(Obj* obj);
        virtual bool boundCorrection(double lft, double rt, double tp, double bt, double t);
        virtual bool collisionCorection(Obj* other);

        virtual ~stdVerlet(){}
};

