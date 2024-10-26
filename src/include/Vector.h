#pragma once
#include <iostream>
#include <vector>
#include <cmath>

class Vector {
private:
    std::vector<double> components;

public:
    // Constructor with initializer list
    Vector();
    Vector(double x, double y, double z);
    Vector(std::initializer_list<double> init);
    Vector(const Vector& p);

    // Access elements
    double& operator[](size_t index);
    const double& operator[](size_t index) const
    ;

    // Get the dimension of the vector
    size_t size() const;

    // Vector addition
    Vector operator+(const Vector& other) const;

    // Vector subtraction
    Vector operator-(const Vector& other) const;

    // Scalar multiplication
    Vector operator*(double scalar) const;

    // Dot product
    double dot(const Vector& other) const;

    // Cross product (only for 3D vectors)
    Vector cross(const Vector& other) const;

    // Magnitude of the vector
    double magnitude() const;

    // Normalize the vector (return unit vector)
    Vector normalize() const;

    double distance(Vector& other) const;

    // Output stream overload for printing
    friend std::ostream& operator<<(std::ostream& os, const Vector& vec);
};
