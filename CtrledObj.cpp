#include "CtrledObj.h"

void CtrledObj::reverseThrust() {
	thrustReverse->start();
	this->acc = (-1) * thrust;
}

void CtrledObj::blockThrust() {
	thrustBlock->start();
	this->acc = 0;
}

void CtrledObj::freeThrust() {
	this->isThrustable = true;
}

void CtrledObj::removeMove() {
	int t = this->v - this->peakV;
	this->v = (t > 0) ? t : 0;
}


void  CtrledObj::startThrust() {
	if (isThrustable) {
		this->acc = thrust;
		thrustCtrl->start();
		this->isThrustable = false;
	}
}

void  CtrledObj::moveForward() {
	if (this->isMovable) {
		this->v += this->peakV;
		this->isMovable = false;
		this->moveCtrl->start();
	}
	else {
		this->moveCtrl->resetAge();
		this->moveCtrl->start();
	}
}

void  CtrledObj::stopForward() {
	if (!this->isMovable) {
		this->moveCtrl->resetAge();
		this->removeMove();
	}
}

void  CtrledObj::turnRight(int d) {
	this->angleScaled -= d;
	this->angleScaled = (this->angleScaled) % (360 * ANGLE_SCALE);
}

void  CtrledObj::turnLeft(int d) {
	this->angleScaled += d;
	this->angleScaled = (this->angleScaled) % (360 * ANGLE_SCALE);
}
