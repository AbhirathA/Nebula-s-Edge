//
// Created by ibrahim on 27/11/24.
//
#include "Meteor.h"

#include <BlackholeObject.h>
#include <PowerUp.h>

#include "Asteroid.h"
#include "Bullet.h"
#include "Enemy.h"
#include "Flare.h"
#include "UserObj.h"

bool Meteor::checkCollision(Asteroid *obj) {
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

bool Meteor::checkCollision(BlackholeObject *obj) {
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

bool Meteor::checkCollision(Meteor *obj) {
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

bool Meteor::checkCollision(Flare *obj) {
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

bool Meteor::checkCollision(PowerUp *obj) {
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

bool Meteor::checkCollision(UserObj *obj) {
    return obj->checkCollision(this);
}

bool Meteor::checkCollision(Enemy *obj) {
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


bool Meteor::checkCollision(Bullet *obj) {
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


bool Meteor::collisionCorrection(Asteroid *obj) {
    return obj->collisionCorrection(this);

}


bool Meteor::collisionCorrection(BlackholeObject *obj) {
    return obj->collisionCorrection(this);
}


bool Meteor::collisionCorrection(Meteor *obj) {
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


bool Meteor::collisionCorrection(PowerUp *obj) {
    return false;
}


bool Meteor::collisionCorrection(Bullet *obj) {
    return obj->collisionCorrection(this);
}


bool Meteor::collisionCorrection(Flare *obj) {
    return obj->collisionCorrection(this);
}


bool Meteor::collisionCorrection(UserObj *obj) {
    return obj->collisionCorrection(this);

}


bool Meteor::collisionCorrection(Enemy *obj) {
    return obj->collisionCorrection(this);
}