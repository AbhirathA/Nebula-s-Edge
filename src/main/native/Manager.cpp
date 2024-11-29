#include "Manager.h"

#include "BlackholeObject.h"

#include "Asteroid.h"
#include "Bullet.h"
#include "Enemy.h"
#include "Meteor.h"
#include "UserObj.h"
#include "PowerUp.h"

std::vector<std::vector<int>> Manager::display(int lowerX, int lowerY, int upperX, int upperY)
{
	std::vector<std::vector<int>> m;
	AABB box = AABB({lowerX, lowerY, 0}, {upperX, upperY, 0});
	std::vector<int> v = tree.boxColliders(&box);
	for (auto id : v)
	{
		int x = objMap[id]->getX();
		int y = objMap[id]->getY();
		std::cout << objMap[id]->getID() << " " << x << " " << y << std::endl;
		int ori = objMap[id]->getOri();
		m.push_back({id, x, y, ori});
	}
	return m;
}

int Manager::dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass, int health, int bulletSpeed, int bulletLife)
{
	UserObj *temp = new UserObj(count, x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown, accX, accY, innerRad, outerRad, mass, health, bulletSpeed, bulletLife);
	this->playerMap[count] = temp;
	this->playerMap[count]->updateAcc(gX, gY);
	this->objMap[count] = this->playerMap[count];
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	count++;
	return count - 1;
}

// int Manager::drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass){
// Obj* temp = new AngleObj(count, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass);
// objMap[count] = temp;
// tree.insert(temp->getObjBox(), count, temp->getStatus());
// temp->updateAcc(gX, gY);
// count++;
// return count-1;
// }

void Manager::update()
{
	std::vector<int> deadObjs = tree.removeDead();
	std::cout << "Dead Objects: ";
	this->removeDead(deadObjs);
	std::cout << deadObjs.size() << std::endl;
	bool flag = true;
	for (auto p : objMap)
	{
		std::cout << p.second->getID() << std::endl;
		p.second->updatePos(t);
		p.second->boundCorrection(lft, rt, tp, bt, t);
		p.second->updateBox();
	}
	tree.Update();
	Lifetime::updateInstances();
	int count = 0;
	while (flag && count++ < precision)
	{
		flag = false;
		std::vector<std::pair<int, int>> collisionPairs = tree.colliderPairs();
		for (auto p : collisionPairs)
		{
			objMap[p.first]->collisionCorrection(objMap[p.second]);
			objMap[p.first]->boundCorrection(lft, rt, tp, bt, t);
			objMap[p.second]->boundCorrection(lft, rt, tp, bt, t);
			objMap[p.first]->updateBox();
			objMap[p.second]->updateBox();
			flag = true;
		}
		tree.Update();
	}
}

int Manager::shoot(int id, int innerRadius, int outerRadius, int mass)
{
	UserObj *curUser = playerMap[id];
	std::tuple<int, int, int, int, int> data = curUser->launchBullet();
	Bullet *temp = new Bullet(count, std::get<0>(data), std::get<1>(data), std::get<2>(data), std::get<3>(data), 0, 0, innerRadius, outerRadius, mass, std::get<4>(data), curUser);
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	objMap[count] = temp;
	count++;
	return count - 1;
}

void Manager::removeDead(std::vector<int> ids)
{
	for (auto id : ids)
	{
		delete objMap[id];
		objMap.erase(id);
	}
}

int Manager::dropAsteroid(int x, int y, int innerRad, int outerRad, int mass)
{
	Obj *temp = new Asteroid(count, x, y, innerRad, outerRad, mass);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count - 1;
}

int Manager::dropBlackHole(int x, int y, int innerRad, int outerRad, int mass)
{
	Obj *temp = new BlackholeObject(count, x, y, innerRad, outerRad, mass);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count - 1;
}

int Manager::dropEnemy(int x, int y, int v, int res, int innerRad, int outerRad, int mass, bool startX, int startSign, int aim)
{
	Obj *temp = new Enemy(count, x, y, v, res, innerRad, outerRad, mass, startX, startSign, objMap[aim]);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count - 1;
}

int Manager::dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass)
{
	Obj *temp = new Meteor(count, x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count - 1;
}

int Manager::dropHealthPowerUp(int x, int y, int radius, int healthIncrease)
{
	PowerUp *temp = new IncreaseHealthPowerUp(count, x, y, radius, healthIncrease);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	count++;
	return count - 1;
}
int Manager::dropBulletBoostPowerUp(int x, int y, int radius, int speedBoost, int lifeBoost, int duration)
{
	PowerUp *temp = new BulletBoostPowerUp(count, x, y, radius, speedBoost, lifeBoost, duration);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	count++;
	return count - 1;
}
int Manager::dropIncreasePointsPowerUp(int x, int y, int radius, int pointScale, int duration)
{
	PowerUp *temp = new IncreasePointsPowerUp(count, x, y, radius, pointScale, duration);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	count++;
	return count - 1;
}

// int main()
// {
// 	Manager manager = Manager(0, 0, 0, 1000, 0, -1000, 1);
// 	int userId1 = manager.dropUser(100, -100, 20, 5, 0, 10, 100, 100, 100, 0, 0, 20, 20, 50, 50, 30, 5);
// 	int bullet1 = manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	manager.shoot(userId1, 5, 5, 1);
// 	while (true)
// 	{
// 		manager.update();
// 	}
// }
