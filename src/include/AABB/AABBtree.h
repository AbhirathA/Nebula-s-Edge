//
// Created by ibrahim on 21/10/24.
//
#include "AABB.h"


class AABBnode {
public:
    AABB box;
    int objIndex;
    AABBnode* parent;
    AABBnode* child1;
    AABBnode* child2;
    bool isLeaf;
    AABBnode() {
        parent = nullptr;
        child1 = nullptr;
        child2 = nullptr;
        isLeaf = false;
    }
    ~AABBnode() {
        delete parent;
        delete child1;
        delete child2;
    }
};


class AABBtree {
private:
    AABBnode* root;
    AABBnode* BestSiblingIndex(AABBnode* leaf);
    AABBnode* BestSibling(AABBnode* leaf);
public:
    AABBtree();
    ~AABBtree();
    void insert(AABB box, int obj);
    void remove(AABB box,int obj);
    AABBnode* find(AABB box, int obj);

};



