#include "FixedObj.h"

// Returns the next position if we update right now.
int FixedObj::getNextX(int t) {
    return this->posX;
}
int FixedObj::getNextY(int t) {
    return this->posY;
}

// No change as fixed obj
void FixedObj::updatePos(int t) {}

// Check if collision has occured
bool FixedObj::checkCollision(Obj* obj) {
    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx * temp2 + dy * dy * temp2);
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > 0) {
        return true;
    }
    return false;
}

// Ensures that the object is in bounds
bool FixedObj::boundCorrection(int lft, int rt, int tp, int bt, int t) {
    return true;
}

// Collision correction
bool FixedObj::collisionCorrection(LinearObj* obj) {

    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx * temp2 + dy * dy * temp2);
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > 0) {

        // Position Correction
        int adjustmentFactor = overlap;
        int adjustmentX = (dx * adjustmentFactor) / distance / temp;
        int adjustmentY = (dy * adjustmentFactor) / distance / temp;

        obj->updateX(obj->getX() + adjustmentX);
        obj->updateY(obj->getY() + adjustmentY);


        // Velocity Correction
        obj->updateV((-1)*(obj->getvX()), -1 * (obj->getY()));

        return true;
    }
    return false;
}
