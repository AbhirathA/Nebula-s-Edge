#include "Manager.h"

std::map<int, std::pair<int, int>> Manager::display() {
	std::map<int, std::pair<int, int>> temp = {};
	for (auto i : this->objMap) {
		temp[i.first] = {(i.second)->getX(),(i.second)->getY()};
	}
	for (auto i : temp) {
		std::cout << i.first << " " << (i.second).first << " " << (i.second).second << std::endl;
	}
	return temp;
}

int Manager::drop1(int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass) {
	if ((((lft <= x) && (x <= rt)) && ((bt <= y) && (y <= tp))) == false) {
		//std::cout << lft << " " << rt << " ! " << bt << " " << tp << std::endl;
		//std::cout << ((lft <= x) && (x <= rt)) << " " << (bt <= y <= tp) << std::endl;
		return false;
	}
	Obj* temp = new stdVerlet(count, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass);
	for(auto& obj : objList){
		if(obj->checkCollision(temp)){
			delete temp;
            return false;
        }
	}
	objList.push_back(temp);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	// temp->updateAcc(gX, gY);
	count++;
	return count-1;
}

int Manager::drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass) {
	Obj* temp = new velVerlet(count, x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
	objList.push_back(temp);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	// temp->updateAcc(gX, gY);
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

void Manager::removeDead(std::vector<int> ids)
{
	for (auto id : ids)
	{
		delete objMap[id];
		objMap.erase(id);
	}
}