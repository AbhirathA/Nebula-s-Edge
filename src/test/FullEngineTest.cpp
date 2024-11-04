#include "../include/ParticleSimulator.h"

int main() {
    ParticleSimulator sim(1500, 1000, {0,0,0});
    for(int i=0; i<60; i++) {
        sim.addParticle(200, 100+11*i, 0, 5, 50, 0, 0, 20);
    }
    for(int i=0; i<60; i++) {
        sim.addParticle(400, 100+11*i, 0, 5, -50, 0, 0, 10);
    }
    sim.run();

}
