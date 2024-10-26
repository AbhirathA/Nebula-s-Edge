#include "../include/AABB/AABB.h"

AABB::AABB(Vector lowerBound, Vector upperBound) : lowerBound(lowerBound), upperBound(upperBound){}

AABB::AABB() = default;

Vector AABB::getUpperBound() {
    return upperBound;
}

Vector AABB::getLowerBound() {
    return lowerBound;
}

double AABB::SA() {
    Vector s = upperBound - lowerBound;
    return 2.0*(s[0]*s[1]+s[1]*s[2]+s[2]*s[0]);
}

AABB AABB::Union(AABB &other) {
    AABB C;
    C.lowerBound = {std::min(lowerBound[0], other.getLowerBound()[0]), std::min(lowerBound[1], other.getLowerBound()[1]), std::min(lowerBound[2], other.getLowerBound()[2])};
    C.upperBound = {std::max(upperBound[0], other.getUpperBound()[0]), std::max(upperBound[1], other.getUpperBound()[1]), std::max(lowerBound[2], other.getUpperBound()[2])};
    return C;
}

bool AABB::Contains(AABB &other) {
    Vector v = other.getUpperBound();
    Vector s = this->getUpperBound();
    const float eps = 1e-8;
    if((v[0] <= s[0] && v[1] <= s[1] && v[2] <= s[2]) || (abs(v[0] - s[0])<=eps && (v[1] - s[1])<=eps && (v[2] - s[2])<= eps)) {
        v = this->getLowerBound();
        s = other.getLowerBound();
        if(v[0] <= s[0] && v[1] <= s[1] && v[2] <= s[2] || (abs(v[0] - s[0])<=eps && (v[1] - s[1])<=eps && (v[2] - s[2])<= eps)) {
            return true;
        }
    }
    return false;
}





