#pragma once
#include <cmath>
#include "AABBtree.h"

class Bullet;
class Meteor;
class Asteroid;
class Flare;
class PowerUp;
class UserObj;
class Enemy;
class BlackholeObject;

class LinearObj;
class AngleObj;

/*
    This class contains all the basic properties every object should have.
    It also looks at basic functionality that every object should have.
*/

class Obj
{
protected:
    // Constants
    static double const PI;                  // Constant for PI value
    static int const VALUE_SCALE = 1000;      // Scale factor for values
    static int const ANGLE_SCALE = 10;        // Scale factor for angle values
    static int const SCALE = 1000;            // General scaling factor
    static int const PRECISION = 1;           // Precision factor for calculations

    int id = 0;                              // Unique ID for each object

    // The X-Y coordinates of the object
    int posX = 0;                            // X position of the object
    int posY = 0;                            // Y position of the object

    // The state of the object (e.g., active, inactive, etc.)
    int state = 0;                           // Current state of the object
    int stateCount = 2;                      // Total number of possible states (for cycling)

    // Mass and radii properties for the object
    int mass = 0;                            // Mass of the object
    int innerRad = 0;                        // Inner radius (for collision or interaction range)
    int outerRad = 0;                        // Outer radius (for collision or interaction range)

    // The bounding box of the object for collision detection
    AABB *objBox = nullptr;                  // Axis-Aligned Bounding Box (AABB) of the object

    // Status of the object (alive or dead)
    bool *dead;                              // Pointer to track if the object is dead

public:
    // Constructor to initialize the object with given parameters
    Obj(int id, int x, int y, int innerRad, int outerRad, int mass)
    {
        this->id = id;
        this->posX = x;
        this->posY = y;
        this->innerRad = innerRad;
        this->outerRad = outerRad;
        this->mass = mass;
        this->objBox = new AABB({x - outerRad, y - outerRad, 0}, {x + outerRad, y + outerRad, 0});  // Initialize AABB
        this->dead = new bool(false);   // Object starts as alive
    }

    // Method to handle damage taken by the object
    virtual void takeDamage()
    {
        this->selfDestruct();  // Initiate self-destruct sequence when damaged
    }

    // Getter method to retrieve the object ID
    int getID()
    {
        return id;
    }

    // Getter methods for position
    int getX()
    {
        return posX;
    }

    int getY()
    {
        return posY;
    }

    // Method to update the X position of the object
    virtual void updateX(int x)
    {
        posX = x;
    }

    // Method to update the Y position of the object
    virtual void updateY(int y)
    {
        posY = y;
    }

    // Getter methods for radii and mass
    int getOuterR()
    {
        return outerRad;
    }

    int getInnerR()
    {
        return innerRad;
    }

    int getState()
    {
        return state;
    }

    int getMass()
    {
        return mass;
    }

    // Getter for the bounding box (AABB)
    AABB *getObjBox()
    {
        return objBox;
    }

    // Getter for the object’s status (alive or dead)
    bool *getStatus()
    {
        return dead;
    }

    // Method to self-destruct the object (e.g., on collision or damage)
    void selfDestruct()
    {
        // Print debug information (position and radius)
        std::cout << this->innerRad + this->posX << ", " << this->innerRad - this->posX << " "
                  << this->innerRad + this->posY << ", " << this->innerRad - this->posY << std::endl;
        *dead = true;  // Mark the object as dead
    }

    // Method to update the AABB bounding box based on new position and radius
    void updateBox()
    {
        objBox->setLowerBound({posX - outerRad, posY - outerRad, 0});
        objBox->setUpperBound({posX + outerRad, posY + outerRad, 0});
    }

    // Method to update the mass of the object
    void updateMass(int m)
    {
        mass = m;
    }

    // Method to change the state of the object (for cycling states)
    void changeState()
    {
        state = (state + 1) % stateCount;  // Cycle through available states
    }

    // Virtual method to get the orientation (can be overridden by subclasses)
    virtual int getOri()
    {
        return 0;  // Default implementation returns 0
    }

    // Virtual methods for checking collision with different object types (to be overridden by subclasses)
    virtual bool checkCollision(Obj *obj) = 0;
    virtual bool checkCollision(Asteroid *obj) = 0;
    virtual bool checkCollision(BlackholeObject *obj) = 0;
    virtual bool checkCollision(Meteor *obj) = 0;
    virtual bool checkCollision(Flare *obj) = 0;
    virtual bool checkCollision(PowerUp *obj) = 0;
    virtual bool checkCollision(UserObj *obj) = 0;
    virtual bool checkCollision(Enemy *obj) = 0;
    virtual bool checkCollision(Bullet *obj) = 0;

    // Virtual method for handling collision correction (to be overridden by subclasses)
    virtual bool collisionCorrection(Obj *other) = 0;

    // Virtual methods for collision correction with different object types
    virtual bool collisionCorrection(Asteroid *obj) = 0;
    virtual bool collisionCorrection(BlackholeObject *obj) = 0;
    virtual bool collisionCorrection(Meteor *obj) = 0;
    virtual bool collisionCorrection(PowerUp *obj) = 0;
    virtual bool collisionCorrection(Bullet *obj) = 0;
    virtual bool collisionCorrection(Flare *obj) = 0;
    virtual bool collisionCorrection(UserObj *obj) = 0;
    virtual bool collisionCorrection(Enemy *obj) = 0;

    // Virtual methods for updating the object's position and acceleration
    virtual void updatePos(int t) = 0;
    virtual void updateAcc(int ax, int ay) = 0;

    // Virtual methods for calculating the next position of the object
    virtual int getNextX(int t) = 0;
    virtual int getNextY(int t) = 0;

    // Method for correcting the object's position if it goes out of bounds
    virtual bool boundCorrection(int lft, int rt, int tp, int bt, int t) = 0;

    // Destructor to clean up dynamically allocated memory
    virtual ~Obj()
    {
        delete objBox;  // Delete the bounding box
        delete dead;     // Delete the status pointer (dead or alive)
    };
};
