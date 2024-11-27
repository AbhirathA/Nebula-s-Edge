#include "Tracker.h"
#include <cmath>
#define PRECISION 1
#define SCALE 1000

void Tracker::updatePos(int t)
{

	// get distance from target.
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	int distance = std::sqrt(dx * dx + dy * dy);

	// point the velocity in the right direction.
	vX = (velocity * dx) / distance;
	vY = (velocity * dy) / distance;

	// move the object towards the target
	int temp = posX;
	posX = posX + vX * t + accX * t * t / 2;

	temp = posY;
	posY = posY + vY * t + accY * t * t / 2;
}

int Tracker::getNextX(int t)
{

	// get distance from target.
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	int distance = std::sqrt(dx * dx + dy * dy);

	// point the velocity in the right direction.
	vX = velocity * dx / distance;

	// move the object towards the target
	return posX + vX * t + accX * t * t / 2;
}
int Tracker::getNextY(int t)
{

	// get distance from target.
	int dx = target->getX() - posX;
	int dy = target->getY() - posY;
	int distance = std::sqrt(dx * dx + dy * dy);

	// point the velocity in the right direction.
	vY = velocity * dy / distance;

	// move the object towards the target
	return posY + vY * t + accY * t * t / 2;
}

bool Tracker::checkCollision(Obj *obj)
{

	// get distance from the target.
	int dx = (posX - obj->getX());
	int dy = (posY - obj->getY());
	int dist = std::sqrt(dx * dx + dy * dy);

	// check if there is an overlap
	if (dist < innerRad + obj->getInnerR() - PRECISION)
	{
		return true;
	}
	return false;
}

bool Tracker::collisionCorrection(Obj *obj)
{

	// get the distance from the object.
	int dx = (posX - obj->getX());
	int dy = (posY - obj->getY());
	int dist = std::sqrt(dx * dx + dy * dy);

	if (dist < innerRad + obj->getOuterR() - PRECISION)
	{

		// calculate new position.
		int overlap = innerRad + obj->getOuterR() - dist + PRECISION;

		this->posX += (overlap * dx) / dist + overlap / 100;
		this->posY += (overlap * dy) / dist + overlap / 200;
	}
	return false;
}

bool Tracker::boundCorrection(int lft, int rt, int tp, int bt, int t)
{
	int x = (this->posX - rt) % (rt - lft + 1) + lft;
	int y = (this->posY - bt) % (bt - tp + 1) + tp;
	bool flag = (x == this->posX && y == this->posY) ? false : true;
	if (flag)
	{
		this->posX = x;
		this->posY = y;
	}
	return flag;
}

#undef PRECISION
#undef SCALE