#pragma once
#include "AngleObj.h"
#include "Lifetime.h"
class CtrledObj : public AngleObj {
	private:
		int direction = 0;
		int peakV = 0;
		int thrust = 0;
		Lifetime* thrustCtrl = nullptr;
		Lifetime* thrustReverse = nullptr;
		Lifetime* moveCtrl = nullptr;
		Lifetime* thrustBlock = nullptr;

		bool isThrustable = true;
		bool isMovable = true;

		void reverseThrust(){
			thrustReverse->start();
			this->acc = (-1)*thrust;
		}

		void blockThrust(){
			thrustBlock->start();
			this->acc = 0;
		}

		void freeThrust(){
			this->isThrustable = true;	
		}

		void removeMove(){
			int t = this->v - this->peakV;
			this->v = (t>0)?t:0;
		}

	public:
		CtrledObj(int id, int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass):AngleObj(id, x, y, driftV, angle, 0, accX, accY, innerRad, outerRad, mass) {
			thrustCtrl = new Lifetime(thrustPersistance, [this]()-> void {this->reverseThrust();
		});
			thrustReverse = new Lifetime(thrustPersistance, [this]()-> void {this->blockThrust();
		});
			moveCtrl = new Lifetime(movePersistance, [this]()-> void {this->removeMove();
		});
			thrustBlock = new Lifetime(coolDown, [this]()-> void {this->freeThrust();
		});

			this->peakV = peakV;
			this->thrust = thrust;
		}

		void startThrust(){
			if(isThrustable){
				this->acc = thrust;
				thrustCtrl->start();
				this->isThrustable = false;
			}
		}

		virtual ~CtrledObj() {}


};
