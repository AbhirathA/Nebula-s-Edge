#include "AngleObj.h"
#include <iostream>

int AngleObj::SIN[3600] = {};
int AngleObj::COS[3600] = {};
bool AngleObj::initialized = false;

void AngleObj::initializeTrig() {
    if (!initialized) {
        for (int i = 0; i < 3600; i++) {
            SIN[i] = sinf((i * Obj::PI) / 180.0 / ANGLE_SCALE) * VALUE_SCALE;
            COS[i] = cosf((i * Obj::PI)/180.0/ ANGLE_SCALE) * VALUE_SCALE;
        }
        initialized = true;
    }
    return;
}

void AngleObj::updateV(int vX, int vY, int scale){

    long long X = vX;
    long long Y = vY;

    double v = sqrt(X*X + Y*Y);
    if (v <= 0.0001 && v>= -0.0001) {
        this->v = 0;
        return;
    }
    //std::cout << "\n\nvel :" << v << std::endl << std::endl;
    double t = asin(vY / v);

    //std::cout << "big idiot: " << (int)((((t * 180) / PI)) * ANGLE_SCALE)%(360 * ANGLE_SCALE) << " " << v;
    if ((0 < (this->angleScaled - 900)) && ((this->angleScaled - 900) < 10)) {
        //std::cout << "sfggd";
    }
    if (vX >= 0 && vY >= 0) {
        this->angleScaled = (((t * 180) / Obj::PI) * ANGLE_SCALE);
    }
    else if(vX >= 0 && vY < 0) {
        this->angleScaled = (((t * 180) / Obj::PI) * ANGLE_SCALE) + 3600;
    }
    else if (vX < 0 && vY >= 0) {
        this->angleScaled = 1800 - (((t * 180) / Obj::PI) * ANGLE_SCALE);
    }
    else {
        this->angleScaled = 1800 - (((t * 180) / Obj::PI) * ANGLE_SCALE);
    }
    this->v = (v+VALUE_SCALE-1)/VALUE_SCALE;
}

bool AngleObj::checkCollision(Obj* obj){ //////////////////////////////
    return obj->checkCollision(this);
}
bool AngleObj::checkCollision(LinearObj* obj) {
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


bool AngleObj::checkCollision(AngleObj* obj){
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


bool AngleObj::collisionCorrection(Obj* other){
    return other->collisionCorrection(this);
}


bool AngleObj::collisionCorrection(LinearObj* obj){
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
        int v2x = obj->getvX(), v2y = obj->getvY();

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
        obj->updateV(v2x / temp2, v2y / temp2);
        //std::cout << "In collision after: " << this->posX << " " << this->posY << " velocity:" << this->getvX() / VALUE_SCALE << " " << this->getvY() / VALUE_SCALE << std::endl;
        obj->updateBox();
        return true;
    }
    return false;
}

bool AngleObj::collisionCorrection(AngleObj* obj){
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
        return true;
    }
    return false;
}


void AngleObj::updatePos(int t){

    // Position is updated
    // std::cout << "Before update: ";
    // printProp();
    this->posX = this->getNextX(t);
    this->posY = this->getNextY(t);


    //std::cout << "mid update: " << " v: " << v << " vX: " << this->getvX() << " vY: " << this->getvY() << " angle: " << angleScaled << std::endl;
    // Velocity is updated
    int vX = this->getvX() + this->getaccX() * t;
    int vY = this->getvY() + this->getaccY() * t;

    //this->v = sqrt(vX * vX + vY * vY) / VALUE_SCALE;
    this->updateV(vX, vY, VALUE_SCALE);
    // std::cout << "after update: ";
    // printProp();
    this->updateBox();
}
int AngleObj::getNextX(int t) {
    return this->posX + (this->getvX() * t + (this->getaccX() * t * t) / 2) / VALUE_SCALE;
}
int AngleObj::getNextY(int t) {
    return this->posY + (this->getvY() * t + (this->getaccY() * t * t) / 2) / VALUE_SCALE;
}

bool AngleObj::boundCorrection(int lft, int rt, int tp, int bt, int t) {
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
    //std::cout << "in bound correction after:" << posX << " " << posY << std::endl;
    this->updateBox();
    return flag;

}
