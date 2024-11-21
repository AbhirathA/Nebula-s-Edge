//
// Created by ibrahim on 21/11/24.
//

#pragma once

class Manager{
  Manager();
  public: void deleteHelper(){

  }
};


class Lifetime {
    protected:
      int maxLife;
      int age;
    public:
      Lifetime(int life){
        this->maxLife = life;
        this->age = 0;
      }
      void grow(Manager manager, int n = 1){
        age += n;
        if(age>=maxLife){
          manager.deleteHelper();
          delete this;
        }
      }
      virtual ~Lifetime();
};



