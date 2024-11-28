#include "Manager.h"

#include <BlackholeObject.h>

#include "Asteroid.h"
#include "Bullet.h"
#include "Enemy.h"
#include "Meteor.h"
#include "UserObj.h"

std::map<int, std::pair<int, int>> Manager::display() {
	// Only Game Render
	std::map<int, std::pair<int, int>> temp = {};
	for (auto i : this->objMap) {
		temp[i.first] = {(i.second)->getX(),(i.second)->getY()};
	}
	for (auto i : temp) {
		//std::cout << i.first << " " << (i.second).first << " " << (i.second).second << std::endl;
	}
	return temp;
}

std::vector<std::vector<int>> Manager::display(int lowerX, int lowerY, int upperX, int upperY) {
	std::vector<std::vector<int>> m;
	AABB box = AABB({lowerX, lowerY, 0}, {upperX, upperY, 0});
	std::vector<int> v = tree.boxColliders(&box);
	for(auto id: v) {
		int x = objMap[id]->getX();
		int y = objMap[id]->getY();
		int ori = objMap[id]->getOri();
		m.push_back({id, x, y, ori});
	}
	return m;
}

int Manager::dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass, int health, int bulletSpeed, int bulletLife) {
	UserObj* temp = new UserObj(count, x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown, accX, accY, innerRad, outerRad, mass, health, bulletSpeed, bulletLife);
	this->playerMap[count] = temp;
	this->playerMap[count]->updateAcc(gX,gY);
	this->objMap[count] = this->playerMap[count];
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	count++;
	return count-1;
}

// int Manager::drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass){
	// Obj* temp = new AngleObj(count, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass);
	// objMap[count] = temp;
	// tree.insert(temp->getObjBox(), count, temp->getStatus());
	// temp->updateAcc(gX, gY);
	// count++;
	// return count-1;
// }

void Manager::update() {
	std::cout << 1 << std::endl;
	std::vector<int> deadObjs = tree.removeDead();
	this->removeDead(deadObjs);
	std::cout << 1 << std::endl;
	bool flag = true;
	for (auto p : objMap) {
		p.second->updatePos(t);
	std::cout << 1 << std::endl;
		p.second->boundCorrection(lft, rt, tp, bt, t);
	std::cout << 1 << std::endl;
		p.second->updateBox();
	std::cout << 1 << std::endl;
	}
	tree.Update();
	std::cout << 1 << std::endl;
	Lifetime::updateInstances();

	std::cout << 1 << std::endl;
	int count = 0;
	while(flag && count++ < precision){
		flag = false;
		std::vector<std::pair<int,int>> collisionPairs = tree.colliderPairs();
		for(auto p: collisionPairs) {
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

int Manager::shoot(int id, int innerRadius, int outerRadius, int mass){
		UserObj* curUser = playerMap[id];
		std::tuple<int, int, int, int, int> data = curUser->launchBullet();
		Bullet* temp = new Bullet(count, std::get<0>(data), std::get<1>(data), std::get<2>(data), std::get<3>(data), 0, 0, innerRadius, outerRadius, mass, std::get<4>(data), curUser);
		tree.insert(temp->getObjBox(), count, temp->getStatus());
		temp->updateAcc(gX, gY);
		objMap[count] = temp;
		count++;
		return count-1;
	}




void Manager::removeDead(std::vector<int> ids) {
	for (auto id : ids)
	{
		delete objMap[id];
		objMap.erase(id);
	}
}

int Manager::dropAsteroid(int x, int y, int innerRad, int outerRad, int mass) {
	Obj* temp = new Asteroid(count, x, y, innerRad, outerRad, mass);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count-1;
}

int Manager::dropBlackHole(int x, int y, int innerRad, int outerRad, int mass) {
	Obj* temp = new BlackholeObject(count, x, y, innerRad, outerRad, mass);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count-1;

}

int Manager::dropEnemy(int x, int y, int v, int res, int innerRad, int outerRad, int mass, bool startX, int startSign, Obj* aim) {
	Obj* temp = new Enemy(count, x, y, v, res, innerRad, outerRad, mass, startX, startSign, aim);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count-1;
}

int Manager::dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass) {
	Obj* temp = new Meteor(count, x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	temp->updateAcc(gX, gY);
	count++;
	return count-1;
}


int main() {
	Manager manager = Manager(0, 0, 0, 1000, 0, -1000, 1);
	int userId1 = manager.dropUser(100, -100, 20, 5, 0, 10, 100, 100, 100, 0, 0, 20, 20, 50, 50, 30, 5);
	int bullet1 = manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	manager.shoot(userId1, 5, 5, 1);
	while(true) {
		manager.update();
	}
}


