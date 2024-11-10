#pragma once
#include "Vector.h"
#include <SFML/Graphics.hpp>
#include "./AABB/AABBtree.h"

enum BoundaryDirection {UP, DOWN, LEFT, RIGHT };

class Particle {
public:
    static int count;
    Vector position;
    Vector a0;
    Vector a1;
    Vector a2;
    Vector velocity;
    AABB* boundingBox;
    int id;
    double radius;
    double collisionRadius{};
    double mass;


    Particle(const Vector& position, const Vector& v, double r, double m);
    Particle();
    Particle(const Particle& p);
    void setPosition(const Vector& position);
    void update(float dt, Vector& acceleration);
    static bool checkCollision(Particle& p1, Particle& p2, double e);
    void draw(sf::RenderWindow& window, sf::Color c = sf::Color::Green) const;
    bool checkBoundary(BoundaryDirection direction, double boundary);
    Particle& operator=(const Particle& p);
    ~Particle();
};