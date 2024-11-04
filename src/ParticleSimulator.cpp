
#include "include/ParticleSimulator.h"

void ParticleSimulator::addParticle(const double x, const double y, const double z, const double radius, const double Vx,
                 const double Vy, const double Vz, double mass) {
    Vector position(x, y, z);
    Vector velocity(Vx, Vy, Vz);
    Particle particle(position, velocity, radius, mass);
    // std::cout<<particle.id<<std::endl;
    particles[particle.id] = particle;
    // std::cout<<particle.id<<std::endl;
    tree.insert(particle.boundingBox->fatBox(0.1), particle.boundingBox, particle.id);
    // std::cout<<particle.id<<std::endl;
}

void ParticleSimulator::update(double dt) {
    for (auto &[id, particle]: particles) {
        particle.update(dt, gravity);
    }
    tree.Update();
    // int c = 0;
    // tree.countLeaves(c);
    // std::cout<<"Particle count: "<<c<<std::endl;
}

void ParticleSimulator::draw() {
    window.clear();
    for (const auto &particle: particles) {
        particle.second.draw(window);
    }
    window.display();
}

void ParticleSimulator::checkBoundaries() {
    for (auto &[id, particle]: particles) {
        particle.checkBoundary(UP, 0);
        // std::cout<<std::endl;
        particle.checkBoundary(DOWN, window.getSize().y);
        particle.checkBoundary(LEFT, 0);
        particle.checkBoundary(RIGHT, window.getSize().x);
    }
}

void ParticleSimulator::checkCollisions() {
    for (auto it1 = particles.begin(); it1 != particles.end(); ++it1) {
        for (auto it2 = std::next(it1); it2 != particles.end(); ++it2) {
            Particle::checkCollision(it1->second, it2->second, 0.9999);
        }
    }
}

void ParticleSimulator::collisionPrune() {
    std::vector<std::pair<int, int>> colliderPairs = tree.colliderPairs();
    checkCollisions(colliderPairs);
}

void ParticleSimulator::checkCollisions(std::vector<std::pair<int, int> > &colliderPairs) {
    std::cout<<colliderPairs.size()<<std::endl;
    for(auto pair : colliderPairs) {
        Particle::checkCollision(particles[pair.first], particles[pair.second], 0.3);
    }
}



void ParticleSimulator::run() {
    window.setFramerateLimit(50);
    while (window.isOpen()) {
        sf::Event event{};
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
            }
        }
        double dt = 0.02f; // Adjust the time step as per your requirement
        checkBoundaries();
        collisionPrune();
        // checkCollisions();
        // addParticle(20, 20, 0, 5, 10, 0, 0, 20);
        std::cout<<"run"<<std::endl;
        update(dt);
        draw();
    }
}
