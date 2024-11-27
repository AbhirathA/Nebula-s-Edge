//
// Created by ibrahim on 26/11/24.
//

#include "Health.h"

int Health::getHealth() {
    return currentHealth;
}


void Health::takeDamage(int damage){
    if(currentHealth - damage <= 0){
        onDeath();
    }
    this->currentHealth -= damage;
}

void Health::heal(int points){
    this->currentHealth += points;
    if(currentHealth > maxHealth){
        currentHealth = maxHealth;
    }
}