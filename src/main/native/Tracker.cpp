#include "Tracker.h"
#include <cmath>


void Tracker::updatePos(int t) {

	// get distance from target.
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	float distance = std::sqrt(dx*dx + dy*dy);

	// point the velocity in the right direction.
	vX = (velocity * dx) / distance;
	vY = (velocity * dy) / distance;

	// move the object towards the target
	int temp = posX;
	posX = temp + vX * t + accX*t*t/2;

	temp = posY;
	posY = temp + vY * t + accY*t*t/2;
	std::cout<<"hi"<<std::endl;
	this->updateBox();
}

int Tracker::getNextX(int t) {
 
	// get distance from target.
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	double distance = std::sqrt(dx * dx + dy * dy);

	// point the velocity in the right direction.
	double X = (velocity * dx) / distance;

	// move the object towards the target
	return posX + X * t + accX * t * t / 2;
}
int Tracker::getNextY(int t) {

	// get distance from target.
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	double distance = std::sqrt(dx * dx + dy * dy);

	// point the velocity in the right direction.
	double Y = (velocity * dy) / distance;

	// move the object towards the target
	return posY + Y * t + accY * t * t / 2;
}

bool Tracker::boundCorrection(int lft, int rt, int tp, int bt, int t) {
	bool flag = false;
	//std::cout << "in bound correction before:" << posX << " " << posY << " Bounds are" << lft << " " << rt << " " << tp << " " << bt << std::endl;
	while (posX > rt) {
		posX = posX - rt + lft + 1;
		flag = true;
	}
	while (posX < lft) {
		posX = posX + rt - lft - 1;
		flag = true;
	}
	while (posY > tp) {
		posY = posY - tp + bt + 1;
		flag = true;
	}
	while (posY < bt) {
		posY = posY + tp - bt - 1;
		flag = true;
	}
	//std::cout << "in bound correction after:" << posX << " " << posY << std::endl;
	this->updateBox();
	return flag;

}

#undef PRECISION
#undef SCALE