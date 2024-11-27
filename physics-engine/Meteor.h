//
// Created by ibrahim on 27/11/24.
//

#include "velVerlet.h"
// #include "Bullet.h"
// #include "Flare.h"
// #include "User.h"
#include "Tracker.h"

#ifndef METEOR_H
#define METEOR_H



class Meteor: public velVerlet {
public:
    Meteor(int id, int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass): velVerlet(id, x, y, vX, vY, accX, accY, innerRad, outerRad, mass){}

};



#endif //METEOR_H
