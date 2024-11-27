#include "Vector.h"

// Constructor with default values
Vector::Vector() = default;

// Constructor with three components
Vector::Vector(int x, int y, int z) : components({x, y, z}) {}

// Constructor with initializer list
Vector::Vector(std::initializer_list<int> init) : components(init) {}

// Copy constructor
Vector::Vector(const Vector& p) : components(p.components) {}

// Access elements
const int& Vector::operator[](size_t index) const
{
    if (index >= components.size()) {
        throw std::out_of_range("Index out of range");
    }
    return components[index];
}

int& Vector::operator[](size_t index) {
    if (index >= components.size()) {
        throw std::out_of_range("Index out of range");
    }
    return components[index];  // Return a reference to the element so it can be modified
}


// Get the dimension of the vector
size_t Vector::size() const {
    return components.size();
}

// Vector addition
Vector Vector::operator+(const Vector& other) const {
    if (components.size() != other.size()) {
        throw std::invalid_argument("Vectors must have the same size");
    }
    Vector result;
    for (size_t i = 0; i < components.size(); ++i) {
        result.components.push_back(components[i] + other[i]);
    }
    return result;
}

// Vector subtraction
Vector Vector::operator-(const Vector& other) const {
    if (components.size() != other.size()) {
        throw std::invalid_argument("Vectors must have the same size");
    }
    Vector result;
    for (size_t i = 0; i < components.size(); ++i) {
        result.components.push_back(components[i] - other[i]);
    }
    return result;
}

// Scalar multiplication
Vector Vector::operator*(int scalar) const {
    Vector result;
    for (size_t i = 0; i < components.size(); ++i) {
        result.components.push_back(components[i] * scalar);
    }
    return result;
}

// Dot product
int Vector::dot(const Vector& other) const {
    if (components.size() != other.size()) {
        throw std::invalid_argument("Vectors must have the same size");
    }
    int result = 0;
    for (size_t i = 0; i < components.size(); ++i) {
        result += components[i] * other[i];
    }
    return result;
}

// Cross product (only for 3D vectors)
Vector Vector::cross(const Vector& other) const {
    if (components.size() != 3 || other.size() != 3) {
        throw std::invalid_argument("Cross product is only defined for 3D vectors");
    }
    return Vector{
        components[1] * other[2] - components[2] * other[1],
        components[2] * other[0] - components[0] * other[2],
        components[0] * other[1] - components[1] * other[0]
    };
}

// Magnitude of the vector
int Vector::magnitude() const {
    int mag = 0;
    for (const int& component : components) {
        mag += component * component;
    }
    return std::sqrt(mag);
}

// Normalize the vector (return unit vector)
Vector Vector::normalize() const {
    int mag = magnitude();
    if (mag == 0) {
        throw std::runtime_error("Cannot normalize a zero vector");
    }
    return *this * (1 / mag);
}

int Vector::distance( Vector& other) const {
  if (components.size() != 3 || other.size() != 3) {
    throw std::invalid_argument("Vectors must have the same size");
    }
   Vector res = *this-other;
   return res.magnitude();
}

// Output stream overload for printing
std::ostream& operator<<(std::ostream& os, const Vector& vec) {
    os << "(";
    for (size_t i = 0; i < vec.size(); ++i) {
        os << vec[i];
        if (i < vec.size() - 1) {
            os << ", ";
        }
    }
    os << ")";
    return os;
}
