//
// Created by ibrahim on 27/11/24.
//


#pragma once
#include "Utilities.h"


class Meteor: public velVerlet {
public:
    Meteor(int id, int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass): velVerlet(id, x, y, vX, vY, accX, accY, innerRad, outerRad, mass){}

};



