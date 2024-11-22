// FixedObj.h
#pragma once
#include "LinearObj.h"

class FixedObj : public LinearObj
{
public:
	FixedObj(int id, int x, int y, int innerRad, int outerRad, int mass)
		: LinearObj(id, x, y, 0, 0, 0, 0, 0, innerRad, outerRad, mass) {}

	virtual ~FixedObj() {}

	// Overridden methods
	void updatePos(int t) override;
	int getNextX(int t) override;
	int getNextY(int t) override;
	bool checkCollision(Obj *obj) override;
	bool boundCorrection(int lft, int rt, int tp, int bt, int t) override;
	bool collisionCorection(Obj *other) override;
};
