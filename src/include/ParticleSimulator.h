#include <SFML/Graphics.hpp>
#include "Particle.h"
#include "AABB/AABBtree.h"
// Define a ParticleSimulator class
class ParticleSimulator {
public:
    sf::RenderWindow window;
    std::map<int, Particle> particles;
    Vector gravity;

    ParticleSimulator(const int width, const int height, const Vector &gravity1)
        : window(sf::VideoMode(width, height), "ParticleSimulator"), gravity(gravity1) {
        AABBtree tree = AABBtree();
    }

    void addParticle(const double x, const double y, const double z, const double radius, const double Vx,
                     const double Vy, const double Vz, double mass);

    void update(double dt);

    void draw();

    void checkBoundaries();

    void checkCollisions();

    void run();

    void checkCollisions(std::vector<std::pair<int, int>> &colliderPairs);

private:
    AABBtree tree;
    void collisionPrune();
};



