//
// Created by ibrahim on 21/11/24.
//

#pragma once
#include <iostream>
#include <functional>
class Manager{
  Manager();
  public: void deleteHelper(){

  }
};


class Lifetime {
    protected:
      int maxLife;
      int age;
      std::function<void()> onExpire();

    public:
      Lifetime(int life){
        this->maxLife = life;
        this->age = 0;
      }
      virtual ~Lifetime();
};



