#pragma once
#include "CtrledObj.h"
#include "Obj.h"
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


		void heal(int points);;

		virtual void takeDamage() final;

		std::tuple<int,int,int,int,int> launchBullet();

		void incrementKillCount();

	virtual bool checkCollision(Obj* obj) override {
		return obj->checkCollision(this);
	}
	virtual bool checkCollision(Asteroid *obj) override;
	virtual bool checkCollision(BlackholeObject * obj) override;
	virtual bool checkCollision(Meteor* obj) override;
	virtual bool checkCollision(Flare* obj) override;
	virtual bool checkCollision(PowerUp* obj) override;
	virtual bool checkCollision(UserObj* obj) override;
	virtual bool checkCollision(Enemy* obj) override;
	virtual bool checkCollision(Bullet* obj) override;

	virtual bool collisionCorrection(Obj* obj) override{
		return obj->collisionCorrection(this);
	}
	virtual bool collisionCorrection(Asteroid* obj) override;
	virtual bool collisionCorrection(BlackholeObject* obj) override;
	virtual bool collisionCorrection(Meteor* obj) override;
	virtual bool collisionCorrection(PowerUp* obj) override;
	virtual bool collisionCorrection(Bullet* obj) override;
	virtual bool collisionCorrection(Flare* obj) override;
	virtual bool collisionCorrection(UserObj* obj) override;
	virtual bool collisionCorrection(Enemy* obj) override;

	int getHealth() {
		return this->healthBar->getHealth();
	}

	int getPoints() {
		return this->totalPoints;
	}

	void scaleUpPoints(int newPoints) {
		this->pointFactor *= newPoints;
	}

	void scaleDownPoints(int newPoints) {
		this->pointFactor /= newPoints;
    }

	void incBulletSpeed(int newBulletSpeed) {
		this->bulletSpeed += newBulletSpeed;
    }

	void decBulletSpeed(int newBulletSpeed) {
        this->bulletSpeed -= newBulletSpeed;
    }

	void incBulletLife(int newBulletLife){
		this->bulletLife += newBulletLife;
    }

	void decBulletLife(int newBulletLife){
        this->bulletLife -= newBulletLife;
    }

};

