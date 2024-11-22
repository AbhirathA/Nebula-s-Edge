import com.physics.FixedObj;
import com.physics.Obj;

public class TestPhysicsEngine {
    public static void main(String[] args) {
        // Load the native library
        System.loadLibrary("PhysicsEngine");

        FixedObj fixedObj = new FixedObj(1, 0, 10, 10, 1000, 10);
        System.out.println("FixedObj " + fixedObj.getY());

        // // Print the initial position
        System.out.println("FixedObj Position: (" + fixedObj.getX() + ", " +
                fixedObj.getY() + ")");

        // // Test methods
        fixedObj.updatePos(1);
        double nextX = fixedObj.getNextX(1);
        double nextY = fixedObj.getNextY(1);
        System.out.println("FixedObj Next Position: (" + nextX + ", " + nextY + ")");

        // // Create another FixedObj to test collision
        // FixedObj otherObj = new FixedObj(2, 5, 5, 10, 10, 1000);
        // boolean collision = fixedObj.checkCollision(otherObj);
        // System.out.println("Collision with otherObj: " + collision);

        // // Perform collision correction
        // boolean corrected = fixedObj.collisionCorection(otherObj);
        // System.out.println("Collision Corrected: " + corrected);

    }
}

// FixedObj fixedObj = new FixedObj(0, 10, 10, 1000);
// ^
// required: int,double,double,double,double,double
// found: int,int,int,int
// reason: actual an