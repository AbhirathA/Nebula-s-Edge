package com.physics;

public class Main {
    public static void main(String[] args) {
        // Load the native library
        System.loadLibrary("PhysicsEngine"); // Ensure this matches your library name

        // Create an instance of FixedObj
        FixedObj fixedObj = new FixedObj(1, 0.0, 0.0, 10.0, 10.0, 1000.0);

        // Print the initial position
        System.out.println("FixedObj Position: (" + fixedObj.getX() + ", " + fixedObj.getY() + ")");

        // Call methods to test functionality
        fixedObj.updatePos(1.0); // Should have no effect for FixedObj
        double nextX = fixedObj.getNextX(1.0);
        double nextY = fixedObj.getNextY(1.0);
        System.out.println("FixedObj Next Position: (" + nextX + ", " + nextY + ")");

        // Since FixedObj doesn't move, positions should remain the same
        boolean inBounds = fixedObj.boundCorrection(0.0, 100.0, 0.0, 100.0, 1.0);
        System.out.println("FixedObj In Bounds: " + inBounds);

        // Create another FixedObj to test collision
        FixedObj otherObj = new FixedObj(2, 5.0, 5.0, 10.0, 10.0, 1000.0);
        boolean collision = fixedObj.checkCollision(otherObj);
        System.out.println("Collision with otherObj: " + collision);

        // Perform collision correction (should have no effect for FixedObj)
        boolean corrected = fixedObj.collisionCorection(otherObj);
        System.out.println("Collision Corrected: " + corrected);
    }
}
