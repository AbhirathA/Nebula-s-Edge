#include "Tracker.h"
#include <cmath>


void Tracker::updatePos(int t) {
	if (target != nullptr) {
		int dx = target->getX() - posX;
		int dy = target->getY() - posY;
		float distance = std::sqrt(dx*dx + dy*dy);
		if (distance > 0) {
			vX = (velocity * dx) / distance;
			vY = (velocity * dy) / distance;
		}
	}
	int temp = posX;
	posX = temp + vX * t + accX*t*t/2;
	temp = posY;
	posY = temp + vY * t + accY*t*t/2;
	this->updateBox();
}

int Tracker::getNextX(int t) {
	if (target == nullptr) return posX + vX * t + accX * t * t / 2;
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	double distance = std::sqrt(dx * dx + dy * dy);
	if (distance <= 0) return posX;
	double X = (velocity * dx) / distance;
	return posX + X * t + accX * t * t / 2;
}
int Tracker::getNextY(int t) {
	if (target == nullptr) return posY + vY * t + accY * t * t / 2;
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	double distance = std::sqrt(dx * dx + dy * dy);
	if (distance <= 0) return posY;
	double Y = (velocity * dy) / distance;
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