#include "Manager.h"

#include "Bullet.h"
#include "UserObj.h"

std::vector<std::vector<int>> Manager::display(int lowerX, int lowerY, int upperX, int upperY)
{
	std::vector<std::vector<int>> m;
	AABB box = AABB({lowerX, lowerY, 0}, {upperX, upperY, 0});
	std::vector<int> v = tree.boxColliders(&box);
	for (auto id : v)
	{
		int x = objMap[id]->getX();
		int y = objMap[id]->getY();
		int ori = objMap[id]->getOri();
		m.push_back({id, x, y, ori});
	}
	return m;
}

// int Manager::drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad, int mass){
// 	Obj* temp = new AngleObj(count, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass);
// 	objMap[count] = temp;
// 	tree.insert(temp->getObjBox(), count, temp->getStatus());
// 	temp->updateAcc(gX, gY);
// 	count++;
// 	return count-1;
// }

void Manager::update()
{
	std::vector<int> deadObjs = tree.removeDead();
	this->removeDead(deadObjs);
	bool flag = true;
	for (auto [j, i] : objMap)
	{

		i->updatePos(t);
		i->boundCorrection(lft, rt, tp, bt, t);
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
