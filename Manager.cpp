#include "Manager.h"

std::map<int, std::pair<int, int>> Manager::display() {
	std::map<int, std::pair<int, int>> temp = {};
	for (auto i : this->objMap) {
		temp[i.first] = {(i.second)->getX(),(i.second)->getY()};
	}
	for (auto i : temp) {
		//std::cout << i.first << " " << (i.second).first << " " << (i.second).second << std::endl;
	}
	return temp;
}

int Manager::dropP(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance, int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass) {
	this->player = new CtrledObj(-1, x, y, peakV, driftV, angle, thrust, thrustPersistance, movePersistance, coolDown, accX, accY, innerRad, outerRad, mass);
	this->player->updateAcc(gX,gY);
	objList.push_back(this->player);
	objMap[-1] = this->player;
	return -1;
}

int Manager::drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass){
	Obj* temp = new AngleObj(count, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass);
	objList.push_back(temp);
	objMap[count] = temp;
	temp->updateAcc(gX, gY);
	count++;
	return count-1;
}

int Manager::drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass) {
	Obj* temp = new velVerlet(count, x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
	objList.push_back(temp);
	objMap[count] = temp;
	temp->updateAcc(gX, gY);
	count++;
	return count-1;
}

void Manager::update() {
	
	bool flag = true;
	for (auto i : objList) {
		i->updatePos(t);
		i->boundCorrection(lft, rt, tp, bt, t);
	}
	Lifetime::updateInstances();
	
	int count = 0;
	while(flag && count++ < precision){
		flag = false;
		for (int i = 0; i < objList.size()-1; i++) {
            for (auto j = i+1; j < objList.size(); j++) {
                if(objList[i]->checkCollision(objList[j])){
                    objList[i]->collisionCorrection(objList[j]);
					objList[i]->boundCorrection(lft, rt, tp, bt, t);
					objList[j]->boundCorrection(lft, rt, tp, bt, t);
                    flag = true;
                }
            }
        }
	}
}