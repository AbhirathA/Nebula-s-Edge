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
	std::cout << "hi";
	this->isMovable = true;
	int t = this->v - this->peakV;
	this->v = (t > driftV) ? t : driftV;
}


void  CtrledObj::startThrust() {
	if (isThrustable) {
		this->acc = thrust;
		thrustCtrl->start();
		this->isThrustable = false;
	}
}

void  CtrledObj::moveForward() {
	printProp();
	if (this->isMovable) {
		std::cout << "first";
		this->v += this->peakV;
		this->isMovable = false;
		this->moveCtrl->start();
	}
	else {
		std::cout << "running";
		this->moveCtrl->resetAge();
		this->moveCtrl->start();
	}
	printProp();
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
