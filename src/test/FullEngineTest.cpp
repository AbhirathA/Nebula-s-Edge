#include "../include/ParticleSimulator.h"

int main() {
    ParticleSimulator sim(1900, 1800, {0,0,0});
    for(int i=0; i<1; i++) {
        sim.addParticle(200, 100, 0, 5, 1000, 100, 0, 20);
    }
    for(int i=0; i<0; i++) {
        sim.addParticle(400, 200+11*i, 0, 5, -1000, 0, 0, 10);
    }
    sim.run();

}
