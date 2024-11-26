//
// Created by ibrahim on 21/10/24.
//
#include <bits/stdc++.h>
#include "AABBtree.h"

// AABBnode greater(AABBnode a, AABBnode b) {
//
// }

AABBtree::AABBtree() {
    root = nullptr;
}

AABBnode* AABBtree::BestSiblingIndex(AABBnode* leaf) {
    std::queue<AABBnode*> Q;
    int bestCost = leaf->box.Union(root->box).SA();
    Q.push(root);
    AABBnode* bestSibling = root;
    while(!Q.empty()) {
        AABBnode* node = Q.front();
        Q.pop();
        AABB uni = node->box.Union(leaf->box);
        int newCost = uni.SA();
        AABBnode* p = node->parent;
        while(p!=nullptr) {
            newCost += p->box.Union(uni).SA() - p->box.SA();
            p = p->parent;
        }
        if(newCost > bestCost){
            continue;
        }
        bestCost = newCost;
        bestSibling = node;
        Q.push(node->child1);
        Q.push(node->child2);

    }
    return bestSibling;
}

AABBnode* AABBtree::BestSibling(AABBnode* leaf) {
    int Cost = leaf->box.Union(root->box).SA();
    AABBnode* bestSibling = root;
    while(!bestSibling->isLeaf) {
        AABBnode* node = bestSibling;
        AABB uni = node->box.Union(leaf->box);
        int cost1 = Cost-node->box.SA()+node->child1->box.Union(leaf->box).SA();
        int cost2 = Cost-node->box.SA()+node->child2->box.Union(leaf->box).SA();
        if(cost1>Cost && cost2>Cost) {
            return node;
        }
        if(cost1>=cost2) {
            bestSibling = node->child2;
            Cost = cost2;
        }
        else {
            bestSibling = node->child1;
            Cost = cost1;
        }
    }
    return bestSibling;
}

int AABBtree::insert(AABB* objBox, int id, bool* status) {
    auto leaf = new AABBnode();
    leaf->objBox = objBox;
    leaf->bound = 50;
    leaf->isLeaf = true;
    leaf->id = id;
    leaf->dead = status;
    leaf->updateAABB();
    if(root==nullptr) {
        root = leaf;
        return 0;
    }
    AABBnode* bestSibling = BestSibling(leaf);
    AABBnode* oldParent = bestSibling->parent;
    auto newParent = new AABBnode();
    newParent->box = leaf->box.Union(bestSibling->box);
    newParent->isLeaf = false;
    if(oldParent == nullptr) {
        root = newParent;
    }
    else {
        if(oldParent->child1 == bestSibling) {
            oldParent->child1 = newParent;
            newParent->parent = oldParent;
        }
        else {
            oldParent->child2 = newParent;
            newParent->parent = oldParent;
        }
    }
    newParent->child1 = bestSibling;
    newParent->child2 = leaf;
    bestSibling->parent = newParent;
    leaf->parent = newParent;
    AABBnode* p = newParent;
    while(p != nullptr) {
        p->box = p->child2->box.Union(p->child1->box);
        p = p->parent;
    }
    return 0;
}

AABBnode* AABBtree::find(AABB* objBox, int id) {
    if(root == nullptr) {
        return nullptr;
    }
    AABBnode* p = root;
    while(!p->isLeaf) {
        if(p->box.Contains(*objBox)) {
            p = (p->child1->box.Contains(*objBox))?p->child1:p->child2;
        }
        else {
            return nullptr;
        }
    }
    if(id == p->id) return p;
    return nullptr;
}

void AABBtree::removeLeaf(AABBnode* node, int id) {
    AABBnode* p = node;
    if(p == nullptr) {
        return;
    }
    if(!p->isLeaf) return;
    AABBnode* q = p->parent;
    if(q==nullptr) {    //root node to be deleted
        root = nullptr;
        delete p;
        return;
    }
    //make the child as nullptr
    if(q->child1 == p) {
        q->child1 = nullptr;
    }
    else {
        q->child2 = nullptr;
    }
    delete p;
    while(q != nullptr) {
        AABBnode* t = q->parent;
        if(q->child1==nullptr && q->child2!=nullptr) {
            q->parent ? (q->parent->child1 == q ? (q->parent->child1 = q->child2):(q->parent->child2 = q->child2)) : (root = q->child2);
            delete q;
            q = t;
        }
        else if(q->child2==nullptr && q->child1!=nullptr) {
            q->parent ? (q->parent->child1 == q ? (q->parent->child1 = q->child1):(q->parent->child2 = q->child1)) : (root = q->child1);
            delete q;
            q = t;
        }
        else if(q->child2!=nullptr && q->child1!=nullptr) {
            q->box = q->child1->box.Union(q->child2->box);
            q = t;
        }
        else {
            q->parent ? (q->parent->child1 == q? (q->parent->child1=nullptr):(q->parent->child2 = nullptr)) : (root = nullptr);
            delete q;
            q = t;
        }
    }
}

void AABBtree::Update() {
    if(root==nullptr) return;
    if(root->isLeaf) {
        root->updateAABB();
        return;
    }
    std::vector<AABBnode*> invalidNodes;
    collectInvalidNodes(root, invalidNodes);
    if(invalidNodes.empty()) return;
    for(auto node: invalidNodes) {
        AABBnode* parent = node->parent;
        AABBnode* sibling = node->Sibling();

        sibling->parent = parent->parent;
        if(sibling->parent == nullptr) {
            root = sibling;
        }
        else {
            if(sibling->parent->child1 == parent) {
                sibling->parent->child1 = sibling;
            }
            else {
                sibling->parent->child2 = sibling;
            }
        }
        delete parent;
        sibling->updateTree();
        node->updateAABB();
        insert(node->objBox, node->id, node->dead);
        delete node;
    }
}

std::vector<int> AABBtree::removeDead() {
    std::vector<int> ids(0);
    if(root==nullptr) return ids;
    std::vector<AABBnode*> deadNodes;
    collectDeadNodes(root, deadNodes);
    if(deadNodes.empty()) return ids;

    for(auto node: deadNodes) {
        AABBnode* parent = node->parent;
        AABBnode* sibling = node->Sibling();

        sibling->parent = parent->parent;
        if(sibling->parent == nullptr) {
            root = sibling;
        }
        else {
            if(sibling->parent->child1 == parent) {
                sibling->parent->child1 = sibling;
            }
            else {
                sibling->parent->child2 = sibling;
            }
        }
        delete parent;
        sibling->updateTree();
        ids.push_back(node->id);
        delete node;
    }
}

 void AABBtree::collectInvalidNodes(AABBnode* node, std::vector<AABBnode*>&invalidNodes) {
    if(node->isLeaf) {
        if(!node->box.Contains(*(node->objBox))) {
            invalidNodes.push_back(node);
        }
    }
    else {
        collectInvalidNodes(node->child1, invalidNodes);
        collectInvalidNodes(node->child2, invalidNodes);
    }
}

void AABBtree::collectDeadNodes(AABBnode* node, std::vector<AABBnode*>&deadNodes) {
    if(node->isLeaf) {
        if(*(node->dead)) {
            deadNodes.push_back(node);
        }
    }
    else {
        collectDeadNodes(node->child1, deadNodes);
        collectDeadNodes(node->child2, deadNodes);
    }
}

AABBtree::~AABBtree() {
    if(root == nullptr) {
        return;
    }
    std::queue<AABBnode*> Q;
    Q.push(root->child1);
    Q.push(root->child2);
    delete root;
    root = nullptr;
    while(!Q.empty()) {
        auto p = Q.front();
        Q.pop();
        if(p->child1 != nullptr) Q.push(p->child1);
        if(p->child2 != nullptr) Q.push(p->child2);
        delete p;
    }
}

std::vector<std::pair<int, int>> AABBtree::colliderPairs() {
    clearCollisionChecks();
    std::vector<std::pair<int, int>> vp(0);
    if(root == nullptr) return vp;
    collisionCheck(root, vp);
    return vp;
}

void AABBtree::collisionCheck(AABBnode *st, std::vector<std::pair<int, int>> &v) {
    if( st == nullptr|| st->collisionsChecked ||st->isLeaf) return;
    collisionCheck(st->child1, st->child2, v);
    st->collisionsChecked = true;
}

void AABBtree::collisionCheck(AABBnode *st1, AABBnode *st2, std::vector<std::pair<int, int>> &v) {
    if(st1 == nullptr || st2==nullptr) return;
    if(st1->isLeaf) {
        if(st2->isLeaf) {
            if(st1->objBox->collides(*(st2->objBox))) {
                v.emplace_back(st1->id, st2->id);
                return;
            }
        }
        else {
            if(st2->child1->box.collides(st1->box)) collisionCheck(st2->child1, st1, v);
            if(st2->child2->box.collides(st1->box)) collisionCheck(st2->child2, st1, v);
        }
    }
    else{
        if(st2->isLeaf) {
            if(st1->child1->box.collides(st2->box)) collisionCheck(st1->child1, st2, v);
            if(st1->child2->box.collides(st2->box)) collisionCheck(st1->child2, st2, v);
        }
        else {
            if(st1->child1->box.collides(st2->child1->box)) collisionCheck(st1->child1, st2->child1, v);
            if(st1->child2->box.collides(st2->child2->box)) collisionCheck(st1->child2, st2->child2, v);
            if(st2->child2->box.collides(st1->child1->box)) collisionCheck(st2->child2, st1->child1, v);
            if(st2->child1->box.collides(st1->child2->box)) collisionCheck(st2->child1, st1->child2, v);
        }
    }
    collisionCheck(st1, v);
    collisionCheck(st2, v);
}

void AABBtree::clearCollisionChecks() {
    if(root == nullptr) return;
    std::queue<AABBnode*> Q;
    Q.push(root);
    while(!Q.empty()) {
        auto p = Q.front();
        Q.pop();
        if(p->child1 != nullptr) Q.push(p->child1);
        if(p->child2 != nullptr) Q.push(p->child2);
        if (p) p->collisionsChecked = false;
    }
}

void AABBtree::countLeaves(int &c) {
    if(root == nullptr) return;
    std::queue<AABBnode*> Q;
    Q.push(root);
    while(!Q.empty()) {
        auto p = Q.front();
        Q.pop();
        if(p->child1 != nullptr) Q.push(p->child1);
        if(p->child2 != nullptr) Q.push(p->child2);
        if(p->isLeaf) c++;
    }
}

void AABBtree::boxCollidersHelper(AABB *Box, AABBnode* st2, std::vector<int> &v) {
        if(st2==nullptr) return;
        if(st2->isLeaf) {
            if(Box->collides(*(st2->objBox))) {
                v.emplace_back(st2->id);
                return;
            }
        }
        else {
            if(st2->child1->box.collides(*Box)) boxCollidersHelper(Box, st2->child1, v);
            if(st2->child2->box.collides(*Box)) boxCollidersHelper(Box, st2->child2, v);
        }
}

std::vector<int> AABBtree::boxColliders(AABB *Box) {
    std::vector<int> vp(0);
    boxCollidersHelper(Box, root, vp);
    return vp;
}