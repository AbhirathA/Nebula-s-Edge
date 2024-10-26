#include <SFML/Graphics.hpp>
#include "Particle.h"
// Define a ParticleSimulator class
class ParticleSimulator {
public:
    sf::RenderWindow window;
    std::vector<Particle> particles;
    Vector gravity;


    ParticleSimulator(const int width, const int height, const Vector &gravity1)
        : window(sf::VideoMode(width, height), "ParticleSimulator"), gravity(gravity1) {
    }

    void addParticle(const double x, const double y, const double z, const double radius, const double Vx,
                     const double Vy, const double Vz, double mass);

    void update(double dt);

    void draw();

    void checkBoundaries();

    void checkCollisions();

    void run();
};



