//
// Created by ibrahim on 21/10/24.
//
#include <bits/stdc++.h>
#include "../include/AABB/AABBtree.h"

// AABBnode greater(AABBnode a, AABBnode b) {
//
// }

AABBtree::AABBtree() {
    root = nullptr;
}

AABBnode* AABBtree::BestSiblingIndex(AABBnode* leaf) {
    std::queue<AABBnode*> Q;
    double bestCost = leaf->box.Union(root->box).SA();
    Q.push(root);
    AABBnode* bestSibling = root;
    while(!Q.empty()) {
        AABBnode* node = Q.front();
        Q.pop();
        AABB uni = node->box.Union(leaf->box);
        double newCost = uni.SA();
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
    double Cost = leaf->box.Union(root->box).SA();
    AABBnode* bestSibling = root;
    while(!bestSibling->isLeaf) {
        AABBnode* node = bestSibling;
        AABB uni = node->box.Union(leaf->box);
        double cost1 = Cost-node->box.SA()+node->child1->box.Union(leaf->box).SA();
        double cost2 = Cost-node->box.SA()+node->child2->box.Union(leaf->box).SA();
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


void AABBtree::insert(AABB box, AABB* objBox) {
    auto leaf = new AABBnode();
    leaf->box = box;
    leaf->objBox = objBox;
    leaf->isLeaf = true;
    if(root==nullptr) {
        root = leaf;
        return;
    }
    AABBnode* bestSibling = BestSiblingIndex(leaf);
    AABBnode* oldParent = bestSibling->parent;
    auto newParent = new AABBnode();
    newParent->box = box.Union(bestSibling->box);
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
}

AABBnode* AABBtree::find(AABB box, AABB* objBox) {
    if(root == nullptr) {
        return nullptr;
    }
    AABBnode* p = root;
    while(!p->isLeaf) {
        if(p->box.Contains(box)) {
            p = (p->child1->box.Contains(box))?p->child1:p->child2;

        }
        else {
            return nullptr;
        }
    }
    if(p->objBox == objBox) return p;
    return nullptr;
}

void AABBtree::removeLeaf(AABBnode* node) {
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
        node->updateAABB();
        insert(node->box, node->objBox);
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
        if(p != nullptr) delete p;
    }
}

