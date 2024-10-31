//
// Created by ibrahim on 21/10/24.
//
#pragma once
#include "AABB.h"


class AABBnode {
public:
    AABB box;
    AABB* objBox;
    AABBnode* parent;
    AABBnode* child1;
    AABBnode* child2;
    double bound;
    bool isLeaf;
    AABBnode() {
        objBox = nullptr;
        parent = nullptr;
        child1 = nullptr;
        child2 = nullptr;
        isLeaf = true;
    }

    void updateAABB() {
        if(isLeaf) {
            Vector boundV(bound, bound, bound);
            box.setLowerBound(objBox->getLowerBound() - boundV);
            box.setUpperBound(objBox->getUpperBound() + boundV);
        }
        else {
            box = this->child1->box.Union(this->child2->box);
        }
    }

    AABBnode* Sibling() {
        return this->parent ? (this->parent->child1==this ? this->parent->child2 : this->parent->child1) : nullptr;
    }
};


class AABBtree {
private:
    AABBnode* root;
    AABBnode* BestSiblingIndex(AABBnode* leaf);
    AABBnode* BestSibling(AABBnode* leaf);
    void collectInvalidNodes(AABBnode*, std::vector<AABBnode*>&);
public:
    AABBtree();
    ~AABBtree();
    int insert(AABB box, AABB* objBox);
    void removeLeaf(AABBnode*);
    void Update();
    AABBnode* find(AABB box, AABB* objBox);

};



