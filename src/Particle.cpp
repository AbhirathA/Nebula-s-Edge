#include "include/Particle.h"

int Particle::count = 0;

// Constructor with position and velocity
Particle::Particle(const Vector& position, const Vector& v, double r, double m) {
    count++;
    this->position = position;
    this->a0 = {0, 0, 0};
    this->a1 = {0, 0, 0};
    this->a2 = {0, 0, 0};
    this->velocity = v;
    this->radius = r;
    this->collisionRadius = r+0.1f;
    this->mass = m;
    boundingBox = new AABB();
    boundingBox->setLowerBound({position[0]-radius, position[1]-radius, 0});
    boundingBox->setUpperBound({position[0]+radius, position[1]+radius, 0});
    id = count-1;
}

// Default constructor
Particle::Particle() {
    position = {0, 0, 0};
    velocity = {0, 0, 0};
    radius = 1.0f;
    collisionRadius = 1.01f;
    mass = 1.0f;
    a0 = {0, 0, 0};
    a1 = {0, 0, 0};
    a2 = {0, 0, 0};
    id = count-1;
    boundingBox = new AABB();
    boundingBox->setLowerBound({position[0]-radius, position[1]-radius, 0});
    boundingBox->setUpperBound({position[0]+radius, position[1]+radius, 0});
}

// Copy constructor
Particle::Particle(const Particle& p) {
    // count++;
    position = p.position;
    a0 = p.a0;
    a1 = p.a1;
    a2 = p.a2;
    velocity = p.velocity;
    radius = p.radius;
    collisionRadius = p.collisionRadius;
    mass = p.mass;
    id = count-1;
    boundingBox = new AABB();
    boundingBox->setLowerBound({position[0]-radius, position[1]-radius, 0});
    boundingBox->setUpperBound({position[0]+radius, position[1]+radius, 0});
}

// Update particle state
void Particle::update(float dt, Vector& force) {
    position = position + velocity * dt + (a1 * 4.0 - a0) * (dt * dt) * (1 / 6.0);
    boundingBox->advance(velocity * dt + (a1 * 4.0 - a0) * (dt * dt) * (1 / 6.0));
    // std::cout<<position<<std::endl;
    // std::cout<<boundingBox->getLowerBound()<<std::endl;
    a2 = force*(1/mass);
    velocity = velocity + (a2 * 2.0 + a1 * 5.0 - a0) * (dt / 6.0);
    // std::cout<<velocity[0]<<" "<<velocity[1]<<" "<<velocity[2]<<std::endl;
    a0 = a1;
    a1 = a2;
}

// Check for collision between particles
bool Particle::checkCollision(Particle& p1, Particle& p2, double e) {
    Vector delta = p2.position - p1.position;
    double dist = p2.position.distance(p1.position);
//    std::cout<<dist<<std::endl;
//    std::cout<<p1.collisionRadius + p2.collisionRadius<<std::endl;
    //
    if (dist < p1.collisionRadius + p2.collisionRadius && dist > 0) {

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

        p1.position = p1.position +  normal*(-1)*((p1.collisionRadius+p2.collisionRadius-dist)/2);
        p2.position = p2.position + normal*((p1.collisionRadius+p2.collisionRadius-dist)/2);
        p1.boundingBox->advance(normal*(-1)*((p1.collisionRadius+p2.collisionRadius-dist)/2));
        p2.boundingBox->advance(normal*((p1.collisionRadius+p2.collisionRadius-dist)/2));
        p1.velocity = normal*_v1n + tangent*v1t;
        p2.velocity = normal*_v2n  + tangent*v2t;

        // p1.setPosition(p1.position + p1.velocity.normalize()*p1.radius*0.5);
        // p2.setPosition(p2.position + p2.velocity.normalize()*p2.radius*0.5);


        return true;
    }
    return false;
}

// Draw the particle
void Particle::draw(sf::RenderWindow& window, sf::Color c ) const {
    sf::CircleShape circle(radius);
    circle.setFillColor(c);
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

void Particle::setPosition(const Vector &position) {
    Particle::position = position;
}

Particle& Particle::operator=(const Particle& p) {
    // count++;
    position = p.position;
    a0 = p.a0;
    a1 = p.a1;
    a2 = p.a2;
    velocity = p.velocity;
    radius = p.radius;
    collisionRadius = p.collisionRadius;
    mass = p.mass;
    id = count-1;
    boundingBox = new AABB();
    boundingBox->setLowerBound({position[0]-radius, position[1]-radius, 0});
    boundingBox->setUpperBound({position[0]+radius, position[1]+radius, 0});
    return *this;
}

Particle::~Particle() {
    delete this->boundingBox;
}