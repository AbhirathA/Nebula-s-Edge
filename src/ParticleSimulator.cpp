
#include "include/ParticleSimulator.h"

void ParticleSimulator::addParticle(const double x, const double y, const double z, const double radius, const double Vx,
                 const double Vy, const double Vz, double mass) {
    Vector position(x, y, z);
    Vector velocity(Vx, Vy, Vz);
    Particle particle(position, velocity, radius, mass);
    // std::cout<<particle.id<<std::endl;
    particles[particle.id] = particle;
    // std::cout<<particle.id<<std::endl;
    tree.insert(particles[particle.id].boundingBox, particle.id);
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
        if(particle.second.id == 76) {
            particle.second.draw(window, sf::Color::Red);
            continue;
        }
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

    if(colliderPairs.size() > 0) {
        std::cout<<colliderPairs.size()<<std::endl;
    }
    if((particles[1].position.distance(particles[0].position)) < 100) {
        std::cout<<"Collision should occur"<<std::endl;
    }
    for(auto pair : colliderPairs) {
        Particle::checkCollision(particles[pair.first], particles[pair.second], 0.3);
    }

}



void ParticleSimulator::run() {
    window.setFramerateLimit(50);
    bool isPaused = false;
    while (window.isOpen()) {
        sf::Event event{};
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
            }
            if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::P)
                isPaused = !isPaused;
        }
        static int count = 0;
        draw();
        if(!isPaused)
        {
            count++;
            if(count<2000) addParticle(1000, 1000, 0, 5, 1000, 0, 0, 20);
            double dt = 0.02f; // Adjust the time step as per your requirement
            update(dt);
            checkBoundaries();
            collisionPrune();
            // checkCollisions();


            std::cout<<"run"<<std::endl;

        }

    }
}
