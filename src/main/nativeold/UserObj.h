#pragma once
#include "CtrledObj.h"
#include "Health.h"
#include <tuple>

class UserObj : public CtrledObj {
private:
	int pointFactor = 10;
	int totalPoints = 0;
	int totalKills = 0;

	int bulletSpeed = 0;
	int bulletLife = 0;


	Health* healthBar = nullptr;

	public:
		UserObj(int id, int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass, int health, int bulletSpeed, int bulletLife) :CtrledObj(id, x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown, accX, accY, innerRad, outerRad, mass) {
			this->healthBar = new Health(health, [this]()->void{this->selfDestruct();});
			this->bulletSpeed = bulletSpeed;
			this->bulletLife = bulletLife;
		}

		virtual ~UserObj() {
			delete healthBar;
		}


		void heal(int points) {
			this->healthBar->heal(points);
		}

		virtual void takeDamage() override {
			this->healthBar->takeDamage(1);
		}

		std::tuple<int,int,int,int,int> launchBullet() {
			return std::make_tuple(this->posX, this->posY, (this->getvX()*bulletSpeed)/this->getV(), (this->getvY()*bulletSpeed)/this->getV(), this->bulletLife);
		}

		void incrementKillCount() {
			totalKills++;
			totalPoints += pointFactor;
		};
};

