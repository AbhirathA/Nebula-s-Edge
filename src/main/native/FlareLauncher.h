//
// Created by ibrahim on 29/11/24.
//

#ifndef FLARELAUNCHER_H
#define FLARELAUNCHER_H
#include <tuple>
#include <vector>
#include "Tracker.h"

class FlareLauncher: {
    protected:
      int curFlareCount;
      int maxFlareCount;
      bool leftRelease = false;
      int flareSpeed;

      std::vector<Tracker*> trackedBy = {};

    public:
      FlareLauncher(int maxFlareCount, int flareSpeed){
          this->curFlareCount = maxFlareCount;
          this->maxFlareCount = maxFlareCount;
          this->flareSpeed = flareSpeed;
      }

      std::tuple<int,int,int,int> launchFlare(int x, int y, int angleScaled){

       }


};



#endif //FLARELAUNCHER_H
