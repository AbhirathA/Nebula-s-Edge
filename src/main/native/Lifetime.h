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
    // One list per thread: every game world runs its simulation on its own thread,
    // so worlds don't age each other's timers or race on a shared vector
    static thread_local std::vector<Lifetime*> instances;

public:
    Lifetime(int life, std::function<void()> callback) {
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
    virtual ~Lifetime() {
        auto it = std::find(instances.begin(), instances.end(), this);
        if (it != instances.end()) instances.erase(it);
    }
};