#pragma once
#include "AngleObj.h"
class CtrledObj : public AngleObj {
	public:
		CtrledObj(int id, int x, int y, int peakV, int driftV int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass):AngleObj(id, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass)
};
