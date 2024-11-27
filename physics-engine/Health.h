//
// Created by ibrahim on 26/11/24.
//

#include <functional>
#pragma once



class Health{
  protected:
    int maxHealth;
    int currentHealth;
    std::function<void()> onDeath;

   public:
     Health(int max, std::function<void()> onDeath){
       this->maxHealth = max;
       this->currentHealth = max;
       this->onDeath = onDeath;
     }

    int getHealth();

     void takeDamage(int damage);

     void heal(int points);





};



