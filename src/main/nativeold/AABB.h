#pragma once
#include "Vector.h"
class AABB{
    Vector lowerBound;
    Vector upperBound;
public:
    AABB(Vector lowerBound, Vector upperBound);
    AABB();
    Vector getLowerBound();
    Vector getUpperBound();
    void setLowerBound(const Vector &v);
    void setUpperBound(const Vector &v);
    void advance(const Vector &v);
    int SA();
    AABB Union(AABB &other);
    bool Contains(AABB &other);
    bool collides(AABB &other);
    AABB fatBox(int t);
};