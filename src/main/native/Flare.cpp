//
// Created by ibrahim on 27/11/24.
//

#include "Flare.h"

#include <BlackholeObject.h>

#include "Asteroid.h"
#include "Enemy.h"
#include "Meteor.h"
#include "UserObj.h"

bool Flare::checkCollision(Asteroid *obj)
{
    return false;
}
bool Flare::checkCollision(BlackholeObject *obj)
{
    return obj->checkCollision(this);
}
bool Flare::checkCollision(Meteor *obj)
{
    return false;
}
bool Flare::checkCollision(Bullet *obj)
{
    return false;
}
bool Flare::checkCollision(PowerUp *obj)
{
    return false;
}
bool Flare::checkCollision(UserObj *obj)
{
    return false;
}
bool Flare::checkCollision(Enemy *obj)
{
    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx + dy * dy) * temp;
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > temp)
    {
        return true;
    }
    return false;
}

// bool Flare::checkCollision(Enemy *obj) {
//     int dx = this->getX() - obj->getX();
//     int dy = this->getY() - obj->getY();
//     int distanceSquared = dx * dx + dy * dy;
//     int attractionRadiusSquared = this->getAttractionRadius() * this->getAttractionRadius();

//     if (distanceSquared <= attractionRadiusSquared) {
//         obj->setTarget(this);
//         return false;
//     }
//     return false;
// }
bool Flare::checkCollision(Flare *obj)
{
    return false;
}

bool Flare::collisionCorrection(Asteroid *obj)
{
    return false;
}
bool Flare::collisionCorrection(BlackholeObject *obj)
{
    return obj->collisionCorrection(this);
}
bool Flare::collisionCorrection(Meteor *obj)
{
    return false;
}
bool Flare::collisionCorrection(PowerUp *obj)
{
    return false;
}
bool Flare::collisionCorrection(Bullet *obj)
{
    return false;
}
bool Flare::collisionCorrection(Flare *obj)
{
    return false;
}
bool Flare::collisionCorrection(UserObj *obj)
{
    return false;
}
bool Flare::collisionCorrection(Enemy *obj)
{
    // Factor because of integer computation instead of floating point
    int temp = 100;
    int temp2 = temp * temp;

    // Get the distance from the object and overlap.
    int dx = this->getX() - obj->getX();
    int dy = this->getY() - obj->getY();
    int distance = sqrt(dx * dx + dy * dy) * temp;
    int overlap = this->getInnerR() * temp + obj->getInnerR() * temp - distance;

    // If the distance is less than the sum of radii, there is a collision.
    if (overlap > temp)
    {
        this->shooter->incrementKillCount();
        this->selfDestruct();
        obj->takeDamage();
        return true;
    }
    return false;
}
