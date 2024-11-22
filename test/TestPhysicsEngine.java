import com.physics.FixedObj;
import com.physics.LinearObject;

public class TestPhysicsEngine {
    public static void main(String[] args) {
        // Load the native library
        System.loadLibrary("PhysicsEngine");

        // LinearObj(int id, double x, double y, double vX, double vY, double accX,
        // double accY, double res, double innerRad, double outerRad, double mass) :
        // Obj(id, x, y, res, innerRad, outerRad, mass)

        FixedObj fixedObj = new FixedObj(1, 0.0, 0.0, 10.0, 10.0, 1000.0);

        // Print the initial position
        System.out.println("FixedObj Position: (" + fixedObj.getX() + ", " + fixedObj.getY() + ")");

        // Test methods
        fixedObj.updatePos(1.0);
        double nextX = fixedObj.getNextX(1.0);
        double nextY = fixedObj.getNextY(1.0);
        System.out.println("FixedObj Next Position: (" + nextX + ", " + nextY + ")");

        // Create another FixedObj to test collision
        FixedObj otherObj = new FixedObj(2, 5.0, 5.0, 10.0, 10.0, 1000.0);
        boolean collision = fixedObj.checkCollision(otherObj);
        System.out.println("Collision with otherObj: " + collision);

        // Perform collision correction
        boolean corrected = fixedObj.collisionCorection(otherObj);
        System.out.println("Collision Corrected: " + corrected);

    }
}
