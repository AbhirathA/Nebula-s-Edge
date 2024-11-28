#pragma once
#include "LinearObj.h"
class FixedObj : public LinearObj {
public:
	FixedObj(int id, int x, int y, int innerRad, int outerRad, int mass):LinearObj(id, x, y, 0, 0, 0, 0, innerRad, outerRad, mass) {}
	virtual void updatePos(int t) override final;
	virtual int getNextX(int t) override final;
	virtual int getNextY(int t) override final;

	virtual bool checkCollision(Obj* obj) override;
	virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t);
	virtual bool collisionCorrection(LinearObj* other);
	virtual void takeDamage() override {
	}



};

