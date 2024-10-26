#pragma once
#include "Vector.h"
#include <SFML/Graphics.hpp>

enum BoundaryDirection { UP, DOWN, LEFT, RIGHT };

class Particle {
public:
    Vector position;
    Vector a0;
    Vector a1;
    Vector a2;
    Vector velocity;
    double radius;
    double collisionRadius{};
    double mass;

    Particle(const Vector& position, const Vector& v, double r, double m);
    Particle();
    Particle(const Particle& p);

    void update(float dt, Vector& acceleration);
    static bool checkCollision(Particle& p1, Particle& p2, double e);
    void draw(sf::RenderWindow& window) const;
    bool checkBoundary(BoundaryDirection direction, double boundary);
};
