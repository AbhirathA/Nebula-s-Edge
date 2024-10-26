#include "include/Particle.h"

// Constructor with position and velocity
Particle::Particle(const Vector& position, const Vector& v, double r, double m) {
    this->position = position;
    this->a0 = {0, 0, 0};
    this->a1 = {0, 0, 0};
    this->a2 = {0, 0, 0};
    this->velocity = v;
    this->radius = r;
    this->collisionRadius = r + (( r > 1) ? 0.001f : 0.001f * r);
    this->mass = m;
}

// Default constructor
Particle::Particle() {
    position = {0, 0, 0};
    a0 = {0, 0, 0};
    a1 = {0, 0, 0};
    a2 = {0, 0, 0};
    velocity = {0, 0, 0};
    radius = 1.0f;
    collisionRadius = 1.01f;
    mass = 1.0f;
}

// Copy constructor
Particle::Particle(const Particle& p) {
    position = p.position;
    a0 = p.a0;
    a1 = p.a1;
    a2 = p.a2;
    velocity = p.velocity;
    radius = p.radius;
    collisionRadius = p.collisionRadius;
    mass = p.mass;
}

// Update particle state
void Particle::update(float dt, Vector& force) {
    position = position + velocity * dt + (a1 * 4.0 - a0) * (dt * dt) * (1 / 6.0);
    a2 = force*(1/mass);
    velocity = velocity + (a2 * 2.0 + a1 * 5.0 - a0) * (dt / 6.0);
    std::cout<<velocity[0]<<" "<<velocity[1]<<" "<<velocity[2]<<std::endl;
    a0 = a1;
    a1 = a2;
}

// Check for collision between particles
bool Particle::checkCollision(Particle& p1, Particle& p2, double e) {
    Vector delta = p2.position - p1.position;
    double dist = p2.position.distance(p1.position);
//    std::cout<<dist<<std::endl;
//    std::cout<<p1.collisionRadius + p2.collisionRadius<<std::endl;
    if (dist < p1.collisionRadius + p2.collisionRadius) {
        Vector normal = delta.normalize();
        Vector tangent({-normal[1], normal[0], 0});

        double v1n = p1.velocity.dot(normal);
        double v2n = p2.velocity.dot(normal);
        double v1t = p1.velocity.dot(tangent);
        double v2t = p2.velocity.dot(tangent);
        double m1 = p1.mass;
        double m2 = p2.mass;

        double _v1n(((m1-m2*e)*v1n + m2*(1+e)*v2n)/(m1+m2));
        double _v2n(((m1+m1*e)*v1n + (m2-m1*e)*v2n)/(m1+m2));

        p1.velocity = normal*_v1n + tangent*v1t;
        p2.velocity = normal*_v2n  + tangent*v2t;

        return true;
    }
    return false;
}

// Draw the particle
void Particle::draw(sf::RenderWindow& window) const {
    sf::CircleShape circle(radius);
    circle.setFillColor(sf::Color::Green);
    circle.setPosition(position[0]-radius, position[1]-radius);
    window.draw(circle);
}

// Check for boundary collisions
bool Particle::checkBoundary(BoundaryDirection direction, double boundary) {
    switch (direction) {
        case DOWN:
            if (position[1] + collisionRadius > boundary && velocity[1] > 0) {
                velocity[1] = -velocity[1];
                return true;
            }
            break;
        case UP:
            if (position[1] - collisionRadius < boundary && velocity[1] < 0) {
                velocity[1] = -velocity[1];
                return true;
            }
            break;
        case RIGHT:
            if (position[0] + collisionRadius > boundary && velocity[0] > 0) {
                velocity[0] = -velocity[0];
                return true;
            }
            break;
        case LEFT:
            if (position[0] - collisionRadius < boundary && velocity[0] < 0) {
                velocity[0] = -velocity[0];
                return true;
            }
            break;
    }
    return false;
}
