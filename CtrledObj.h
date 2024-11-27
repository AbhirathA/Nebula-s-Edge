#pragma once
#include "AngleObj.h"
#include "Lifetime.h"

class CtrledObj : public AngleObj {
	protected:
		int peakV = 0;
		int thrust = 0;

		Lifetime* thrustCtrl = nullptr;
		Lifetime* thrustReverse = nullptr;
		Lifetime* thrustBlock = nullptr;
		Lifetime* moveCtrl = nullptr;

		bool isThrustable = true;
		bool isMovable = true;

		void reverseThrust();
		void blockThrust();
		void freeThrust();
		void removeMove();

	public:
		CtrledObj(int id, int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass) :AngleObj(id, x, y, driftV, angle, 0, accX, accY, innerRad, outerRad, mass) {
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

		void startThrust();

		void moveForward();
		void stopForward();

		void turnRight(int d);
		void turnLeft(int d);

		virtual ~CtrledObj() {
			delete this->thrustCtrl;
			delete this->thrustReverse;
			delete this->thrustBlock;
			delete this->moveCtrl;
		}
};
