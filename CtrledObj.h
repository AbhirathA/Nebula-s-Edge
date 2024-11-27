#pragma once
#include "AngleObj.h"
class CtrledObj : public AngleObj {
	int forward = 0;
	int peakV = 0;
	int thrust = 0;
	Lifetime* thrustCtrl = nullptr;
	Lifetime* thrustReverse = nullptr;
	Lifetime* moveCtrl = nullptr;
	Lifetime* thrustBlock = nullptr;

	bool isThrustable = true;



	public:
		CtrledObj(int id, int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass):AngleObj(id, x, y, driftV, angle, 0, accX, accY, innerRad, outerRad, mass){
			thrustCtrl = new Lifetime(thrustPersistance, reverseThrust());
			thrustReverse = new Lifetime(thrustPersistance, blockThrust());
			moveCtrl = new Lifetime(movePersistance, removeMove());
			thrustBlocker = new Lifetime(coolDown, freeThrust());

			this->peakV = peakV;
			this->thrust = thrust;
		}

		void reverseThrust(){
			thrustReverse.start();
			this->acc = (-1)*thrust;
		}

		void blockThrust(){
			thrustBlocker.start();
			this->isThrustable = false;
			this->acc = 0;
		}

		void freeThrust(){
			this->isThrustable = true;	
		}

		void startThrust(){
			if(isThrustable){
				this->acc = thrust;
				thrustCtrl.start();
				this->isThrustable = false;
			}
		}


};
