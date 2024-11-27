#include "UserObj.h"
void UserObj::heal(int points) {
    this->healthBar->heal(points);
}

void UserObj::takeDamage() {
    this->healthBar->takeDamage(1);
}

std::tuple<int,int,int,int,int> UserObj::launchBullet() {
    return std::make_tuple(this->posX, this->posY, (this->getvX()*bulletSpeed)/this->getV(), (this->getvY()*bulletSpeed)/this->getV(), this->bulletLife);
}

void UserObj::incrementKillCount() {
    totalKills++;
    totalPoints += pointFactor;
};