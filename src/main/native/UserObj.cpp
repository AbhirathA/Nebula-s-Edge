#include "UserObj.h"
#include "Utilities.h"
#include "Enemy.h"
#include "Meteor.h"

void UserObj::heal(int points) {
    this->healthBar->heal(points);
}

void UserObj::takeDamage() {
    this->healthBar->takeDamage(1);
}

std::tuple<int,int,int,int,int> UserObj::launchBullet() {
    return std::make_tuple(this->posX, this->posY, (COS[this->angleScaled]*bulletSpeed)/VALUE_SCALE, (SIN[this->angleScaled]*bulletSpeed)/VALUE_SCALE, this->bulletLife);
}

void UserObj::incrementKillCount() {
    totalKills++;
    totalPoints += pointFactor;
};

bool UserObj::checkCollision(Asteroid *obj) {
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

bool UserObj::checkCollision(BlackholeObject *obj) {
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

bool UserObj::checkCollision(Meteor *obj) {
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

bool UserObj::checkCollision(Flare *obj) {
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

bool UserObj::checkCollision(PowerUp *obj) {
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

bool UserObj::checkCollision(UserObj *obj) {
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

bool UserObj::checkCollision(Enemy *obj) {
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


bool UserObj::checkCollision(Bullet *obj) {
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


bool UserObj::collisionCorrection(Asteroid *obj) {
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
// Factor because of integer computation instead of floating point
        //std::cout << "In collision before: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;

        // Position Correction
        int adjustmentFactor = overlap;
        int adjustmentX = (dx * adjustmentFactor) / distance;
        int adjustmentY = (dy * adjustmentFactor) / distance;

        this->updateX(this->getX() + adjustmentX);
        this->updateY(this->getY() + adjustmentY);

        obj->updateX(obj->getX() - adjustmentX);
        obj->updateY(obj->getY() - adjustmentY);


        // Velocity Correction

        int nx = (dx * temp2) / distance;
        int ny = (dy * temp2) / distance;

        int v1x = this->getvX() / VALUE_SCALE, v1y = this->getvY() / VALUE_SCALE;

        int v1Along = v1x * nx + v1y * ny;

        int v1PerpX = v1x * temp2 - v1Along * nx;
        int v1PerpY = v1y * temp2 - v1Along * ny;

        int v1NewAlong = -v1Along;

        v1x = v1NewAlong * nx + v1PerpX;
        v1y = v1NewAlong * ny + v1PerpY;

        std::cout << "v1x: " << v1x/temp2 << " v1y: " << v1y/temp2 << std::endl;

        this->updateV(v1x , v1y , temp2);
        std::cout << "v1x: " << this->getvX()/VALUE_SCALE << " v1y: " << this->getvY()/VALUE_SCALE << std::endl;
        //std::cout << "In collision after: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;
        this->updateBox();
        this->takeDamage();
        return true;
    }
    return false;

}


bool UserObj::collisionCorrection(BlackholeObject *obj) {
    return obj->checkCollision(this);
}


bool UserObj::collisionCorrection(Meteor *obj) {
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
        //std::cout << "In collision before: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;

        // Position Correction
        int adjustmentFactor = overlap / 2;
        int adjustmentX = (dx * adjustmentFactor) / distance;
        int adjustmentY = (dy * adjustmentFactor) / distance;

        this->updateX(this->getX() + adjustmentX);
        this->updateY(this->getY() + adjustmentY);

        obj->updateX(obj->getX() - adjustmentX);
        obj->updateY(obj->getY() - adjustmentY);


        // Velocity Correction

        int nx = (dx * temp2) / distance;
        int ny = (dy * temp2) / distance;

        int v1x = this->getvX() / VALUE_SCALE, v1y = this->getvY() / VALUE_SCALE;
        int v2x = obj->getvX() , v2y = obj->getvY();

        int m1 = this->getMass();
        int m2 = obj->getMass();

        int v1Along = v1x * nx + v1y * ny;
        int v2Along = v2x * nx + v2y * ny;

        int v1PerpX = v1x * temp2 - v1Along * nx;
        int v1PerpY = v1y * temp2 - v1Along * ny;
        int v2PerpX = v2x * temp2 - v2Along * nx;
        int v2PerpY = v2y * temp2 - v2Along * ny;

        int v1NewAlong = ((m1 - m2) * v1Along + 2 * m2 * v2Along) / (m1 + m2);
        int v2NewAlong = ((m2 - m1) * v2Along + 2 * m1 * v1Along) / (m1 + m2);

        v1x = v1NewAlong * nx + v1PerpX;
        v1y = v1NewAlong * ny + v1PerpY;

        v2x = v2NewAlong * nx + v2PerpX;
        v2y = v2NewAlong * ny + v2PerpY;

        this->updateV(v1x, v1y, temp2);
        obj->updateV(v2x / temp2, v2y / temp2);
        //std::cout << "In collision after: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;
        obj->updateBox();
        this->takeDamage();
        return true;
    }
    return false;
}


bool UserObj::collisionCorrection(PowerUp *obj) {
    return false;
}


bool UserObj::collisionCorrection(Bullet *obj) {
    return obj->collisionCorrection(this);
}


bool UserObj::collisionCorrection(Flare *obj) {
    return false;
}


bool UserObj::collisionCorrection(UserObj *obj) {
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
        //std::cout << "In collision before: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;

        // Position Correction
        int adjustmentFactor = overlap / 2;
        int adjustmentX = (dx * adjustmentFactor) / distance;
        int adjustmentY = (dy * adjustmentFactor) / distance;

        this->updateX(this->getX() + adjustmentX);
        this->updateY(this->getY() + adjustmentY);

        obj->updateX(obj->getX() - adjustmentX);
        obj->updateY(obj->getY() - adjustmentY);


        // Velocity Correction

        int nx = (dx * temp2) / distance;
        int ny = (dy * temp2) / distance;

        int v1x = this->getvX() / VALUE_SCALE, v1y = this->getvY() / VALUE_SCALE;
        int v2x = obj->getvX() / VALUE_SCALE, v2y = obj->getvY() / VALUE_SCALE;

        int m1 = this->getMass();
        int m2 = obj->getMass();

        int v1Along = v1x * nx + v1y * ny;
        int v2Along = v2x * nx + v2y * ny;

        int v1PerpX = v1x * temp2 - v1Along * nx;
        int v1PerpY = v1y * temp2 - v1Along * ny;
        int v2PerpX = v2x * temp2 - v2Along * nx;
        int v2PerpY = v2y * temp2 - v2Along * ny;

        int v1NewAlong = ((m1 - m2) * v1Along + 2 * m2 * v2Along) / (m1 + m2);
        int v2NewAlong = ((m2 - m1) * v2Along + 2 * m1 * v1Along) / (m1 + m2);

        v1x = v1NewAlong * nx + v1PerpX;
        v1y = v1NewAlong * ny + v1PerpY;

        v2x = v2NewAlong * nx + v2PerpX;
        v2y = v2NewAlong * ny + v2PerpY;

        this->updateV(v1x / temp2, v1y / temp2, 1);
        obj->updateV(v2x / temp2, v2y / temp2, 1);
        //std::cout << "In collision after: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;
        obj->updateBox();
        this->takeDamage();
        obj->takeDamage();
        return true;
    }
    return false;
}


bool UserObj::collisionCorrection(Enemy *obj) {
    return obj->collisionCorrection(this);
}