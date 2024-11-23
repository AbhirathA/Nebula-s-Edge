#include "Manager.h"

void Manager::display() {
	
	/*
	std::vector<std::vector<char>> plot(tp-bt+1,std::vector<char>(rt-lft+1, ' ')); ////
	for (auto i : objList) {
		plot[-(i->getY())][i->getX()] = (i->getState()) ? '8' : '0';
	}
	for (auto i : plot) {
		std::cout<<'|';
		for (auto j : i) {
			std::cout << j;
		}
		std::cout<<'|'<<std::endl;
	}
	*/

	std::cout <<objList[0]->getX() << " " << objList[0]->getY() << std::endl;
}

bool Manager::drop1(int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass) {
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
	//temp->updateAcc(gX, gY);
	count++;
	return true;
}

bool Manager::drop2(int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad, int mass) {
	if ((((lft <= x) && (x <= rt)) && ((bt <= y) && (y <= tp))) == false) {
		//std::cout << lft << " " << rt << " ! " << bt << " " << tp << std::endl;
		//std::cout << ((lft <= x) && (x <= rt)) << " " << (bt <= y <= tp) << std::endl;
		return false;
	}
	Obj* temp = new velVerlet(count, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass);
	for (auto& obj : objList) {
		if (obj->checkCollision(temp)) {
			delete temp;
			return false;
		}
	}
	objList.push_back(temp);
	objMap[count] = temp;
	tree.insert(temp->getObjBox(), count, temp->getStatus());
	//temp->updateAcc(gX, gY);
	count++;
	return true;
}

void Manager::update() {
	/*
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
                    objList[i]->collisionCorection(objList[j]);
					objList[i]->boundCorrection(lft, rt, tp, bt, t);
					objList[j]->boundCorrection(lft, rt, tp, bt, t);
                    flag = true;
                }
            }
        }
	}
	*/

}

void Manager::removeDead(std::vector<int> ids){
  for()
}