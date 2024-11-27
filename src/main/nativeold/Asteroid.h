//
// Created by ibrahim on 26/11/24.
//
#include "FixedObj.h"

#pragma once

class Asteroid: public FixedObj {
    public:
        Asteroid(int id, int x, int y, int innerRad, int outerRad, int mass):FixedObj(id, x, y, innerRad, outerRad, mass) {}


};



