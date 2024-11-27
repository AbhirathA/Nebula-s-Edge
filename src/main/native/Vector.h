#pragma once
#include <iostream>
#include <vector>
#include <cmath>

class Vector {
private:
    std::vector<int> components;

public:
    // Constructor with initializer list
    Vector();
    Vector(int x, int y, int z);
    Vector(std::initializer_list<int> init);
    Vector(const Vector& p);

    // Access elements
    int& operator[](size_t index);
    const int& operator[](size_t index) const
    ;

    // Get the dimension of the vector
    size_t size() const;

    // Vector addition
    Vector operator+(const Vector& other) const;

    // Vector subtraction
    Vector operator-(const Vector& other) const;

    // Scalar multiplication
    Vector operator*(int scalar) const;

    // Dot product
    int dot(const Vector& other) const;

    // Cross product (only for 3D vectors)
    Vector cross(const Vector& other) const;

    // Magnitude of the vector
    int magnitude() const;

    // Normalize the vector (return unit vector)
    Vector normalize() const;

    int distance(Vector& other) const;

    // Output stream overload for printing
    friend std::ostream& operator<<(std::ostream& os, const Vector& vec);
};
