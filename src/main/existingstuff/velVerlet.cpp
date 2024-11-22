#include "velVerlet.h"
#include <math.h>

// Returns the next position if we update right now.
double velVerlet::getNextX(double t){
    return this->posX + this->vX * t + 0.5 * this->accX * t * t;
} 
double velVerlet::getNextY(double t){
    return this->posY + this->vY * t + 0.5 * this->accY * t * t;
}

// We are using Velocity Verlet Algorithm for this
void velVerlet::updatePos(double t){

    // Position is updated
    this->posX = this->getNextX(t);
    this->posY = this->getNextY(t);

    // Velocity is updated
    this->vX = this->vX + this->accX * t;
    this->vY = this->vY + this->accY * t;
}

// Check if collision has occured
bool velVerlet::checkCollision(Obj* obj){ /////////////////////////////////////////
    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx * temp2 + dy * dy * temp2);
    int overlap = this->getInnerR()*temp + obj->getInnerR()*temp - distance;
    
    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > 0){
        return true;
    }
    return false;
}

// Ensures that the object is in bounds
bool velVerlet::boundCorrection(double lft, double rt, double tp, double bt, double t){

    int x = (this->posX - rt)%(rt-lft+1) + lft;
    int y = (this->posY - bt)%(bt-tp+1) + tp;
    bool flag = (x==this->posX && y==this->posY)?false:true;
    if(flag){
        this->posX = x;
        this->posY = y;
    }
    return flag;
}

// Collision correction
bool velVerlet::collisionCorection(LinearObj* obj){

    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx * temp2 + dy * dy * temp2);
    int overlap = this->getInnerR()*temp + obj->getInnerR()*temp - distance;
    
    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > 0){

        // Position Correction
        int adjustmentFactor = overlap / 2;
        int adjustmentX = (dx  * adjustmentFactor) / distance / temp;
        int adjustmentY = (dy * adjustmentFactor) / distance / temp;
            
        this->updateX(this->getX() - adjustmentX);
        this->updateY(this->getY() - adjustmentY);
        
        obj->updateX(obj->getX() + adjustmentX);
        obj->updateY(obj->getY() + adjustmentY);


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

        return true;
    }
    return false;
}