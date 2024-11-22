// FixedObj.cpp
#include "FixedObj.h"
#include <cmath>

void FixedObj::updatePos(int t)
{
    // Fixed object does not move
}

int FixedObj::getNextX(int t)
{
    return this->posX;
}

int FixedObj::getNextY(int t)
{
    return this->posY;
}

bool FixedObj::checkCollision(Obj *obj)
{
    int temp = 100;
    int temp2 = temp * temp;

    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = std::sqrt(dx * dx * temp2 + dy * dy * temp2);
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    return overlap > 0;
}

bool FixedObj::boundCorrection(int lft, int rt, int tp, int bt, int t)
{
    return true; // Fixed object doesn't need boundary correction
}

bool FixedObj::collisionCorection(Obj *obj)
{
    int temp = 100;
    int temp2 = temp * temp;

    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = std::sqrt(dx * dx * temp2 + dy * dy * temp2);
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    if (overlap > 0)
    {
        // Attempt to cast obj to LinearObj*
        LinearObj *linearObj = dynamic_cast<LinearObj *>(obj);
        if (linearObj)
        {
            // Position Correction
            int adjustmentFactor = overlap;
            int adjustmentX = (dx * adjustmentFactor) / distance / temp;
            int adjustmentY = (dy * adjustmentFactor) / distance / temp;

            linearObj->updateX(linearObj->getX() + adjustmentX);
            linearObj->updateY(linearObj->getY() + adjustmentY);

            // Velocity Correction
            linearObj->updateV(-linearObj->getvX(), -linearObj->getvY());
        }
        return true;
    }
    return false;
}
