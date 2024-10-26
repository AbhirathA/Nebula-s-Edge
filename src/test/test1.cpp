#include "../include/ParticleSimulator.h"
int main() {
    Vector gravity(1000, 1000, 0);
    ParticleSimulator simulator(1500, 1000, gravity);
    // std::cout<<simulator.window.getSize().x<<" "<<simulator.window.getSize().y<<std::endl;
    // Add particles to the simulator
    // simulator.addParticle(20, 300, 0, 10, 4, 2, 0);
    // simulator.addParticle(50, 30, 0, 10, 2, 1, 0);
    // simulator.addParticle(10, 70, 0, 10, 5, 4, 0);
    simulator.addParticle(200, 200, 0, 50, 1000, 500, 0, 10);
    simulator.addParticle(600, 200, 0, 50, -1000, 1000, 0, 100);
    simulator.run();

    return 0;
}