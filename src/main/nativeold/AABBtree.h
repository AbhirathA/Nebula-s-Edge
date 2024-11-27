//
// Created by ibrahim on 21/10/24.
//
#pragma once
#include "AABB.h"

class AABBnode
{
public:
    AABB box;
    AABB *objBox;
    AABBnode *parent;
    AABBnode *child1;
    AABBnode *child2;
    int id;
    int bound;
    bool isLeaf;
    bool collisionsChecked;
    bool *dead;
    AABBnode()
    {
        objBox = nullptr;
        parent = nullptr;
        child1 = nullptr;
        child2 = nullptr;
        isLeaf = true;
        collisionsChecked = false;
        dead = nullptr;
        id = -1;
    }

    void updateAABB()
    {
        if (isLeaf)
        {
            Vector boundV(bound, bound, 0);
            box.setLowerBound(objBox->getLowerBound() - boundV);
            box.setUpperBound(objBox->getUpperBound() + boundV);
        }
        else
        {
            box = this->child1->box.Union(this->child2->box);
        }
    }

    void updateTree()
    {
        if (parent == nullptr)
            return;
        parent->updateAABB();
        parent->updateTree();
    }

    AABBnode *Sibling()
    {
        return this->parent ? (this->parent->child1 == this ? this->parent->child2 : this->parent->child1) : nullptr;
    }
    ~AABBnode() = default;
};

class AABBtree
{
private:
    AABBnode *root;
    AABBnode *BestSiblingIndex(AABBnode *leaf);
    AABBnode *BestSibling(AABBnode *leaf);
    void collectInvalidNodes(AABBnode *, std::vector<AABBnode *> &);
    void collectDeadNodes(AABBnode *, std::vector<AABBnode *> &);
    void collisionCheck(AABBnode *st1, AABBnode *st2, std::vector<std::pair<int, int>> &);
    void collisionCheck(AABBnode *st, std::vector<std::pair<int, int>> &);
    void clearCollisionChecks();
    void boxCollidersHelper(AABB *Box, AABBnode *st, std::vector<int> &v);

public:
    AABBtree();
    ~AABBtree();
    int insert(AABB *objBox, int id, bool *status);
    void countLeaves(int &c);
    void removeLeaf(AABBnode *, int id);
    void Update();
    std::vector<int> removeDead();
    AABBnode *find(AABB *objBox, int id);
    std::vector<std::pair<int, int>> colliderPairs();
    std::vector<int> boxColliders(AABB *Box);
};
