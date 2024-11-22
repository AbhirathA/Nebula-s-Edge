#include "stdVerlet.h"
#define PRECISION 1
#define SCALE 1000

// We are using Verlet Algorithm for this
void stdVerlet::updatePos(int t) {
	int temp = posX;

	// Update X position
	posX = this->getNextX(t);
	posX_vOld = posX;
	posX_old = temp;

	//Check if we have reached terminal velocity
	vX = (this->getNextX(t) - posX_old)/t/2;
	(abs(vX) == terminalX) ? (isXTerminal = true) : (isXTerminal = false);

	/*
	posX = 2 * posX - posX_old + accX * t * t;
	vX = (this->getNextX(t) - temp)/t/2;

	if(abs(vX) > terminalX) {
		posX = temp;
		isXTerminal = true;
		vX = terminalX;
		posX
	}	
	else{
		posX_vOld = posX_old;
		posX_old = temp;
		isXTerminal = false;
	}
	*/

	temp = posY;

	//Update Y position
	posY = this->getNextY(t);
	posY_vOld = posY_old;
	posY_old = posY;

	//Check if we have reached terminal velocity
	vY = (this->getNextY(t) - posY_old) / t / 2;
	(abs(vY) == terminalY) ? (isYTerminal = true) : (isYTerminal = false);

	/*
	posY = 2 * posY - posY_old + accY * t * t;
	vY = (this->getNextY(t) - temp)/t/2;

	if(abs(vY) > terminalY) {
		posY = temp;
		isYTerminal = true;
		vY = terminalY;
	}
	else{
		posY_vOld = posY_old;
		posY_old = temp;
		isYTerminal = false;
	}
	*/
}

// Check collision with another object.
bool stdVerlet::checkCollision(Obj* obj){
	// Make this pure virtual later

	// get distance along axises
	int dx = obj->getX() - this->posX;
	int dy = obj->getY() - this->posY;


	// get the distance between the centers.
	int distance = std::sqrt(dx * dx + dy * dy);

	// check if gap > sum of radii
	if(distance < obj->getInnerR() + this->innerRad - PRECISION){
		return true;
	}
    return false; 
}


// Returns the next position if we update right now.
int stdVerlet::getNextX(int t){

	// Verlet approximation to get next position.
	int ans = (2 * posX - posX_old + accX * t * t);

	// Look at max distance object can ove due to terminal velocity.
	int max = terminalX * t + posX;
	int min = posX - terminalX * t;

	// return either actual value or the maximum motion due to terminal velocity.
	if (max < ans) {
		return max;
	}
	else if (min > ans) {
		return min;
	}
	return ans;
}
int stdVerlet::getNextY(int t){

	// Verlet approximation to get next position.
	int ans = (2 * posY - posY_old + accY * t * t);

	// Look at max distance object can ove due to terminal velocity.
	int max = terminalY * t + posY;
	int min = posY - terminalY * t;

	// return either actual value or the maximum motion due to terminal velocity.
	if (max < ans) {
		return max;
	}
	else if (min > ans) {
		return min;
	}
	return ans;
}

// Corrects the position if it goes out of bounds.
bool stdVerlet::boundCorrection(int lft, int rt, int tp, int bt, int t){
	// Make this pure virtual later
	bool answer = false;

	// bounce off the boundary assuming the time till collision was t/2. 
	if( posX < lft ){
		posX = posX_old;
		posX_old = 2*posX - posX_vOld;

		vX = (this->getNextX(t) + this->posX_old)/t/2;
		posX_vOld = posX_old - vX*t;
		answer = true;
	}
	else if( posX > rt){
		posX = posX_old;
		posX_old = 2*posX - posX_vOld;

		vX = (this->getNextX(t) + this->posX_old)/t/2;
		posX_vOld = posX_old - vX*t;;
		answer = true;
	}

	if( posY < bt){
		posY = posY_old;
		posY_old = 2*posY - posY_vOld;

		vY = (this->getNextY(t) + this->posY_old)/t/2;
		posY_vOld = posY_old - vY*t;;
	    answer = true;
	}
	else if( posY > tp){
		posY = posY_old;
		posY_old = 2*posY - posY_vOld;

		vY = (this->getNextY(t) + this->posY_old)/t/2;
		posY_vOld = posY_old - vY*t;;
		answer = true;
	}
	return answer;
}

// Corrects the position if collision is detected
bool stdVerlet::collisionCorection(Obj* other){
	// Make this pure virtual later

	// get the gap between the two objects.
    int dx = this->posX - other->getX();
    int dy = this->posY - other->getY();

	// pre-compute the other objects radius.
	int radOther = other->getInnerR();
	// get the distance between the 2 centers
	float distance = std::sqrt(dx * dx + dy * dy);

	// Check 
    if(distance < (radOther + this->outerRad - PRECISION)){

		// Get distance of overlap.
        int overlap = radOther + this->outerRad - distance + PRECISION;

		// Get the factor by which we move the objects.
		int cm = (this->mass + other->getMass());
		int thisFactor = (SCALE * other->getMass()) / cm;
		int otherFactor =  SCALE - thisFactor ;

		// This changes the current object's position. 
		int overlapX = overlap * thisFactor * dx / distance / 2 / SCALE;
		int overlapY = overlap * thisFactor * dy / distance / 2 / SCALE;

        this->posX += overlapX;
        this->posY += overlapY;

		this->posX_old = 2*this->posX - this->posX_old;
		this->posX_vOld = 2*this->posX - this->posX_vOld;

		this->posY_old = 2 * this->posY - this->posY_old;
		this->posY_vOld = 2 * this->posY - this->posY_vOld;

		this->vX = (this->getNextX(1) - this->posX_old) / 2;
		this->vY = (this->getNextY(1) - this->posY_old) / 2;


		// Move the other object and change its velocity.
		overlapX = other->getX() - overlap * otherFactor * dx / distance / 2 / SCALE;
		overlapY = other->getY() - overlap * otherFactor * dy / distance / 2 / SCALE;

		((stdVerlet*)other)->updateX(overlapX, 2 * overlapX - ((stdVerlet*)other)->getX_Old(), 2 * overlapX - ((stdVerlet*)other)->getX_vOld());
		((stdVerlet*)other)->updateX(overlapY, 2 * overlapY - ((stdVerlet*)other)->getY_Old(), 2 * overlapY - ((stdVerlet*)other)->getY_vOld());

		((stdVerlet*)other)->updateV((other->getNextX(1) - ((stdVerlet*)other)->getX_Old()) / 2, (other->getNextY(1) - ((stdVerlet*)other)->getY_Old()) / 2);

		// Change state due to collision being detected.
		this->changeState();
		other->changeState();

		return true;
	}
	return false;
}

#undef PRECISION
#undef SCALE