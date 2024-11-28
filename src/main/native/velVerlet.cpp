#include "velVerlet.h"
#include <math.h>
#include <iostream>


// Returns the next position if we update right now.
int velVerlet::getNextX(int t){
    return this->posX + this->vX * t + (this->accX * t * t)/2;
} 
int velVerlet::getNextY(int t){
    return this->posY + this->vY * t + (this->accY * t * t)/2;
}

// We are using Velocity Verlet Algorithm for this
void velVerlet::updatePos(int t){
    //std::cout << "in update before:" << posX << " " << posY << std::endl;

    // Position is updated
    this->posX = this->getNextX(t);
    this->posY = this->getNextY(t);

    this->updateBox();
    // Velocity is updated
    this->vX = this->vX + this->accX * t;
    this->vY = this->vY + this->accY * t;

    //std::cout << "in update after:" << posX << " " << posY << std::endl;
}

// Check if collision has occured
bool velVerlet::checkCollision(LinearObj* obj){ /////////////////////////////////////////
    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx + dy * dy) * temp;
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > temp) {
        return true;
    }
    return false;
}

// Ensures that the object is in bounds
bool velVerlet::boundCorrection(int lft, int rt, int tp, int bt, int t){
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
    this->updateBox();
    //std::cout << "in bound correction after:" << posX << " " << posY << std::endl;
    return flag;
}

// Collision correction
bool velVerlet::collisionCorrection(LinearObj* obj){

    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx + dy * dy) * temp;
    int overlap = this->getInnerR()*temp + obj->getInnerR()*temp - distance;
    
    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > temp){
        //std::cout << "In collision before: " << this->posX << " " << this->posY << " velocity:" << this->vX << " " << this->vY << std::endl;

        // Position Correction
        int adjustmentFactor = overlap / 2;
        int adjustmentX = (dx  * adjustmentFactor) / distance;
        int adjustmentY = (dy * adjustmentFactor) / distance;
            
        this->updateX(this->getX() + adjustmentX);
        this->updateY(this->getY() + adjustmentY);

        this->updateBox();
        
        obj->updateX(obj->getX() - adjustmentX);
        obj->updateY(obj->getY() - adjustmentY);

        obj->updateBox();
        // Velocity Correction
        
        int nx = (dx*temp2)/ distance;
        int ny = (dy*temp2) / distance;
        
        int v1x = this->getvX(), v1y = this->getvY();
        int v2x = obj->getvX(), v2y = obj->getvY();
        
        int m1 = this->getMass();
        int m2 = obj->getMass();
        
        int v1Along = v1x * nx + v1y * ny;
        int v2Along = v2x * nx + v2y * ny;
        
        int v1PerpX = v1x*temp2 - v1Along * nx;
        int v1PerpY = v1y*temp2 - v1Along * ny;
        int v2PerpX = v2x*temp2 - v2Along * nx;
        int v2PerpY = v2y*temp2 - v2Along * ny;
        
        int v1NewAlong = ((m1 - m2) * v1Along + 2 * m2 * v2Along) / (m1 + m2);
        int v2NewAlong = ((m2 - m1) * v2Along + 2 * m1 * v1Along) / (m1 + m2);
        
        v1x = v1NewAlong * nx + v1PerpX;
        v1y = v1NewAlong * ny + v1PerpY;
        
        v2x = v2NewAlong * nx + v2PerpX;
        v2y = v2NewAlong * ny + v2PerpY;
        
        this->updateV(v1x/temp2, v1y/temp2);
        obj->updateV(v2x/temp2, v2y/temp2);
        //std::cout << "In collision after: " << this->posX << " " << this->posY << " velocity:" << this->vX << " " << this->vY << std::endl;

        return true;
    }
    return false;
}
