//
// Created by ibrahim on 21/11/24.
//

#include "Lifetime.h"

std::vector<Lifetime*> Lifetime::instances = {};


void Lifetime::incrementAge() {
    if (isUpdateable == false) {
        return;
    }
    age++;
    if (age >= maxLife) {
        this->end();
        onExpire();
    }
}

void Lifetime::updateInstances() {
    for (auto ltObj : Lifetime::instances) {
        ltObj->incrementAge();
    }
}

void Lifetime::start() {
    isUpdateable = true;
}

void Lifetime::end() {
    this->age = 0;
    isUpdateable = false;
}

void Lifetime::resetAge() {
    this->age = 0;
    this->isUpdateable = false;
}
