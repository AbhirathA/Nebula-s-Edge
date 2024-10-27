#include "../Vector.h"
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
    double SA();
    AABB Union(AABB &other);
    bool Contains(AABB &other);
};