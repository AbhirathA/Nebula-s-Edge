//
// Created by ibrahim on 21/11/24.
//

#pragma once
#include <iostream>
#include <vector>
#include <algorithm>
#include <functional>


class Lifetime {

    protected:
      int maxLife;
      int age;
      bool isUpdateable;
      std::function<void()> onExpire;
      static std::vector<Lifetime*> instances;

    public:
      Lifetime(int life, std::function<void()> callback){
        this->maxLife = life;
        this->age = 0;
        isUpdateable = false;
        this->onExpire = callback;
        instances.push_back(this);
      }
      static void updateInstances();
      void incrementAge();
      void start();
      void end();
      void resetAge();
      virtual ~Lifetime(){
          instances.erase(find(v.begin(), v.end(), this));
      }
};



