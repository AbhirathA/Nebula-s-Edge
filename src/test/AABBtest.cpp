#include "../include/AABB/AABBtree.h"

int main(){
    auto aabb = AABBtree();
    auto p1 = new AABB({3, 4, 0}, {4,5,0});
    auto p4 = new AABB({2,7,0}, {4,9,0});
    auto p2 = new AABB({4,3,0}, {5,4,0});
    auto p3 = new AABB({3,2,0}, {5,6,0});
    auto p5 = new AABB({15,18,0}, {17,19,0});
    auto p6 = new AABB({16,16,0}, {18,19,0});
    auto p7 = new AABB({17,9,0}, {20,11,0});
    auto f1 = p1->fatBox(0.1);
    auto f2 = p2->fatBox(0.1);
    auto f3 = p3->fatBox(0.1);
    auto f4 = p4->fatBox(0.1);
    auto f5 = p5->fatBox(0.1);
    auto f6 = p6->fatBox(0.1);
    auto f7 = p7->fatBox(0.1);
    int x = aabb.insert(f1, p1);
    aabb.insert(f2, p2);
    aabb.insert(f3, p3);
    aabb.insert(f4, p4);
    aabb.insert(f5, p5);
    aabb.insert(f6, p6);
    aabb.insert(f7, p7);
    // AABBnode* n = aabb.find(f1, p1);
    // aabb.removeLeaf(n);
    p1->advance({1,1,0});
    aabb.Update();
}
