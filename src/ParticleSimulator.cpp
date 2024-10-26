
#include "include/ParticleSimulator.h"

void ParticleSimulator::addParticle(const double x, const double y, const double z, const double radius, const double Vx,
                 const double Vy, const double Vz, double mass) {
    Vector position(x, y, z);
    Vector velocity(Vx, Vy, Vz);
    particles.emplace_back(position, velocity, radius, mass);
}

void ParticleSimulator::update(double dt) {
    for (auto &particle: particles) {
        particle.update(dt, gravity);
    }
}

void ParticleSimulator::draw() {
    window.clear();
    for (const auto &particle: particles) {
        particle.draw(window);
    }
    window.display();
}

void ParticleSimulator::checkBoundaries() {
    for (auto &particle: particles) {
        std::cout<<particle.checkBoundary(UP, 0);
        std::cout<<std::endl;
        particle.checkBoundary(DOWN, window.getSize().y);
        particle.checkBoundary(LEFT, 0);
        particle.checkBoundary(RIGHT, window.getSize().x);
    }
}

void ParticleSimulator::checkCollisions() {
    for (int i = 0; i < particles.size(); i++) {
        for (int j = i + 1; j < particles.size(); j++) {
            Particle::checkCollision(particles[i], particles[j], 0.5);
        }
    }
}

void ParticleSimulator::run() {
    window.setFramerateLimit(100);
    while (window.isOpen()) {
        sf::Event event{};
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
            }
        }
        double dt = 0.01f; // Adjust the time step as per your requirement
        checkBoundaries();
        checkCollisions();
        update(dt);
        draw();
    }
}
