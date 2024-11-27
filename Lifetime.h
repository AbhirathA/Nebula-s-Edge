//
// Created by ibrahim on 21/11/24.
//

#pragma once
#include <iostream>
#include <functional>


class Lifetime {

    protected:
      int maxLife;
      int age;
      std::function<void()> onExpire;

    public:
      Lifetime(int life, std::function<void()> callback){
        this->maxLife = life;
        this->age = 0;
        this->onExpire = callback;
      }
      void incrementAge();
      void resetAge(){
          this->age = 0;
      }
      virtual ~Lifetime();
};



