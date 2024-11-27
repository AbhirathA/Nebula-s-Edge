#pragma once
#include "CtrledObj.h"
class UserObj : public CtrledObj {
	
	
	public:
		//UserObj(int id, int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass, int health) :CtrledObj(id, x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown, accX, accY, innerRad, outerRad, mass) {

		//}

		virtual ~UserObj() {};
};

