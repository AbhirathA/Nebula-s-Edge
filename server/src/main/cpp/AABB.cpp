#include "AABB.h"

AABB::AABB(Vector lowerBound, Vector upperBound) : lowerBound(lowerBound), upperBound(upperBound) {}

AABB::AABB() = default;

Vector AABB::getUpperBound()
{
    return upperBound;
}

Vector AABB::getLowerBound()
{
    return lowerBound;
}

void AABB::setUpperBound(const Vector &v)
{
    upperBound = v;
}

void AABB::setLowerBound(const Vector &v)
{
    lowerBound = v;
}

int AABB::SA()
{
    Vector s = upperBound - lowerBound;
    return 2 * (s[0] * s[1] + s[1] * s[2] + s[2] * s[0]);
}

AABB AABB::Union(AABB &other)
{
    AABB C;
    C.lowerBound = {std::min(lowerBound[0], other.getLowerBound()[0]), std::min(lowerBound[1], other.getLowerBound()[1]), std::min(lowerBound[2], other.getLowerBound()[2])};
    C.upperBound = {std::max(upperBound[0], other.getUpperBound()[0]), std::max(upperBound[1], other.getUpperBound()[1]), std::max(lowerBound[2], other.getUpperBound()[2])};
    return C;
}

bool AABB::Contains(AABB &other)
{
    Vector v = other.getUpperBound();
    Vector s = upperBound;
    if ((v[0] <= s[0] && v[1] <= s[1] && v[2] <= s[2]))
    {
        v = lowerBound;
        s = other.getLowerBound();
        if (v[0] <= s[0] && v[1] <= s[1] && v[2] <= s[2])
        {
            return true;
        }
    }
    return false;
}

AABB AABB::fatBox(int t)
{
    Vector lb = {lowerBound[0] - t, lowerBound[1] - t, lowerBound[2]};
    Vector ub = {upperBound[0] + t, upperBound[1] + t, upperBound[2]};
    return AABB(lb, ub);
}

void AABB::advance(const Vector &v)
{
    lowerBound = lowerBound + v;
    upperBound = upperBound + v;
}

bool AABB::collides(AABB &other)
{
    if (this->lowerBound[0] > other.upperBound[0] || this->upperBound[0] < other.lowerBound[0] || this->lowerBound[1] > other.upperBound[1] || this->upperBound[1] < other.lowerBound[1])
    {
        return false;
    }
    return true;
}
