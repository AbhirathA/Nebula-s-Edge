#pragma once
#include "AngleObj.h"
class CtrledObj : public AngleObj {
	public:
		CtrledObj(int id, int x, int y, int peakV, int driftV int angle, int thrust, int accX, int accY, int innerRad, int outerRad, int mass):AngleObj(id, x, y, driftV, angle, 0, accX, accY, innerRad, outerRad, mass)
};
