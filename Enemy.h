//
// Created by ibrahim on 27/11/24.
//

#ifndef ENEMY_H
#define ENEMY_H
#include "Tracker.h"


class Enemy : public Tracker{
public:
    Enemy(int id, int x, int y, int v, int res, int innerRad, int outerRad, int mass, bool startX, int startSign,
        Obj *aim)
        : Tracker(id, x, y, v, res, innerRad, outerRad, mass, startX, startSign, aim) {
    }
};



#endif //ENEMY_H
