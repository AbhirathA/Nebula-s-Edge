#pragma once
#include "LinearObj.h"
class stdVerlet : public LinearObj{

    protected:
    	int posX_vOld = 0;
		int posY_vOld = 0;
		int posX_old = 0;
		int posY_old = 0;

    public:
        stdVerlet(int id, int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass) : LinearObj(id, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass) {
            
            this->posX_old = x - vX * res;
			this->posY_old = y - vY * res;
			this->posX_vOld = x - 2 * vX * res ; ///
			this->posY_vOld = y - 2 * vY * res ; ///
        }

        int getX_Old(){
            return posX_old;
        }

		int getY_Old(){
            return posY_old;
        }

		int getX_vOld(){
			return posX_vOld;
		}

		int getY_vOld(){
            return posY_vOld;
        }

		void updateX(int x, int o, int vo) {
			posX_vOld = vo;
			posX_old = o;
			posX = x;
		}

		void updateX(int x, int o) {
			posX_vOld = posX_old;
			posX_old = o;
			posX = x;
		}

		void updateX(int x) {
			posX_vOld = posX_old;
			posX_old = posX;
			posX = x;
		}

		void updateY(int y, int o, int vo) {
			posY_vOld = vo;
			posY_old = o;
			posY = y;
		}

		void updateY(int y, int o) {
			posY_vOld = posY_old;
			posY_old = o;
			posY = y;
		}

		void updateY(int y) {
			posY_vOld = posY_old;
			posY_old = posY;
			posY = y;
		}

        virtual void updatePos(int t) override final;
        virtual int getNextX(int t) override final;
        virtual int getNextY(int t) override final;

        virtual bool checkCollision(LinearObj* obj);
		bool checkCollision(Obj* obj) { return false; }
        virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);
        virtual bool collisionCorection(LinearObj* other);
		virtual bool collisionCorection(Obj* other) { return false; }

        virtual ~stdVerlet(){}
};

