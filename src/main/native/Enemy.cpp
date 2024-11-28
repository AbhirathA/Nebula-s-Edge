//
// Created by ibrahim on 27/11/24.
//

#include "Enemy.h"

#include "PowerUp.h"

#include "BlackholeObject.h"

#include "Asteroid.h"
#include "Bullet.h"
#include "Flare.h"
#include "Meteor.h"
#include "UserObj.h"

bool Enemy::checkCollision(Asteroid *obj) {
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
bool Enemy::checkCollision(BlackholeObject *obj) {
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
bool Enemy::checkCollision(Meteor *obj) {
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
bool Enemy::checkCollision(Flare *obj) {
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
bool Enemy::checkCollision(PowerUp *obj) {
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
bool Enemy::checkCollision(UserObj *obj) {
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
bool Enemy::checkCollision(Enemy *obj) {
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
bool Enemy::checkCollision(Bullet *obj) {
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



bool Enemy::collisionCorrection(Asteroid *obj) {
    if(target->getID() == obj->getID()) {
        if(this->checkCollision(target)) {
            this->selfDestruct();
            obj->takeDamage();
        }
        return false;
    }
    // get the distance from the object.
    int dx = (posX - obj->getX()) * 100;
    int dy = (posY - obj->getY()) * 100 ;
    int dist = std::sqrt(dx * dx + dy * dy);

    if (dist < innerRad*100 + obj->getOuterR()*100 - PRECISION) {

        // calculate new position.
        int overlap = innerRad*100 + obj->getOuterR()*100 - dist + PRECISION;

        this->posX += (overlap*dx)/dist/100 + PRECISION;
        this->posY += (overlap*dy)/dist/100 + PRECISION;

        this->updateBox();
    }
    return false;
}
bool Enemy::collisionCorrection(BlackholeObject *obj) {
    if(target->getID() == obj->getID()) {
        if(this->checkCollision(target)) {
            this->selfDestruct();
            obj->takeDamage();
        }
        return false;
    }
    // get the distance from the object.
    int dx = (posX - obj->getX()) * 100;
    int dy = (posY - obj->getY()) * 100 ;
    int dist = std::sqrt(dx * dx + dy * dy);

    if (dist < innerRad*100 + obj->getOuterR()*100 - PRECISION) {

        // calculate new position.
        int overlap = innerRad*100 + obj->getOuterR()*100 - dist + PRECISION;

        this->posX += (overlap*dx)/dist/100 + PRECISION;
        this->posY += (overlap*dy)/dist/100 + PRECISION;

        this->updateBox();
    }
    return false;
}
bool Enemy::collisionCorrection(Meteor *obj) {
    if(target->getID() == obj->getID()) {
        if(this->checkCollision(target)) {
            this->selfDestruct();
            obj->takeDamage();
        }
        return false;
    }
    // get the distance from the object.
    int dx = (posX - obj->getX()) * 100;
    int dy = (posY - obj->getY()) * 100 ;
    int dist = std::sqrt(dx * dx + dy * dy);

    if (dist < innerRad*100 + obj->getOuterR()*100 - PRECISION) {

        // calculate new position.
        int overlap = innerRad*100 + obj->getOuterR()*100 - dist + PRECISION;

        this->posX += (overlap*dx)/dist/100 + PRECISION;
        this->posY += (overlap*dy)/dist/100 + PRECISION;

        this->updateBox();
    }
    return false;
}
bool Enemy::collisionCorrection(PowerUp *obj) {
    return false;
}
bool Enemy::collisionCorrection(Bullet *obj) {
    return obj->collisionCorrection(this);
}
bool Enemy::collisionCorrection(Flare *obj) {
    return obj->collisionCorrection(this);
}
bool Enemy::collisionCorrection(UserObj *obj) {
    if(target->getID() == obj->getID()) {
        if(this->checkCollision(target)) {
            this->selfDestruct();
            obj->takeDamage();
        }
        return false;
    }
    // get the distance from the object.
    int dx = (posX - obj->getX()) * 100;
    int dy = (posY - obj->getY()) * 100 ;
    int dist = std::sqrt(dx * dx + dy * dy);

    if (dist < innerRad*100 + obj->getOuterR()*100 - PRECISION) {

        // calculate new position.
        int overlap = innerRad*100 + obj->getOuterR()*100 - dist + PRECISION;

        this->posX += (overlap*dx)/dist/100 + PRECISION;
        this->posY += (overlap*dy)/dist/100 + PRECISION;

        this->updateBox();
    }
    return false;
}
bool Enemy::collisionCorrection(Enemy *obj) {
    return false;
}