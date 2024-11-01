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
    int id;
    double bound;
    bool isLeaf;
    bool collisionsChecked;
    AABBnode() {
        objBox = nullptr;
        parent = nullptr;
        child1 = nullptr;
        child2 = nullptr;
        isLeaf = true;
        collisionsChecked = false;
        id = -1;
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
    ~AABBnode() = default;
};


class AABBtree {
private:
    AABBnode* root;
    AABBnode* BestSiblingIndex(AABBnode* leaf);
    AABBnode* BestSibling(AABBnode* leaf);
    void collectInvalidNodes(AABBnode*, std::vector<AABBnode*>&);
    void collisionCheck(AABBnode* st1, AABBnode* st2, std::vector<std::pair<int, int>>&);
    void collisionCheck(AABBnode* st, std::vector<std::pair<int,int>>&);
    void  clearCollisionChecks();
public:
    AABBtree();
    ~AABBtree();
    int insert(AABB box, AABB* objBox, int id);
    void removeLeaf(AABBnode*, int id);
    void Update();
    AABBnode* find(AABB box, AABB* objBox, int id);
    std::vector<std::pair<int,int>>& colliderPairs();
};



