//
// Created by ibrahim on 26/11/24.
//
#pragma once

#include "Asteroid.h"

#include <BlackholeObject.h>

#include "Meteor.h"
#include "UserObj.h"
#include "Utilities.h"

bool Asteroid::checkCollision(Asteroid *obj) {
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

bool Asteroid::checkCollision(BlackholeObject *obj) {
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

bool Asteroid::checkCollision(Meteor *obj) {
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

bool Asteroid::checkCollision(Flare *obj) {
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

bool Asteroid::checkCollision(PowerUp *obj) {
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

bool Asteroid::checkCollision(UserObj *obj) {
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

bool Asteroid::checkCollision(Enemy *obj) {
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


bool Asteroid::checkCollision(Bullet *obj) {
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


bool Asteroid::collisionCorrection(Asteroid *obj) {
    return false;
}


bool Asteroid::collisionCorrection(BlackholeObject *obj) {
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


bool Asteroid::collisionCorrection(Meteor *obj) {
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

        obj->updateBox();

        // Velocity Correction
        obj->updateV((-1)*(obj->getvX()), -1 * (obj->getY()));

        return true;
    }
    return false;
}


bool Asteroid::collisionCorrection(PowerUp *obj) {
    return false;
}


bool Asteroid::collisionCorrection(Bullet *obj) {
    return obj->collisionCorrection(this);
}


bool Asteroid::collisionCorrection(Flare *obj) {
    return obj->checkCollision(this);
}


bool Asteroid::collisionCorrection(UserObj *obj) {
    return obj->collisionCorrection(this);

}


bool Asteroid::collisionCorrection(Enemy *obj) {

    return obj->collisionCorrection(this);

}