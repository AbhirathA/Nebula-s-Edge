import com.physics.Manager;

public class TestPhysicsEngine {
    static int factor = 30;

    public static void main(String[] args) {
        System.loadLibrary("PhysicsEngine");

        System.out.println("Physics Engine loaded successfully!");

        Manager manager = new Manager(0, 2, 0, 1000, 0, -1000, 1);

        int c = 5;
        while (c != 0) {
            c--;
            manager.update();

            int[][] data = manager.display(50, 100, 200, 300);

            for (int[] entry : data) {
                int id = entry[0];
                int x = entry[1];
                int y = entry[2];
                int ori = entry[3];
                System.out.println("ID: " + id + ", X: " + x + ", Y: " + y + ", Orientation:" + ori);
            }
        }

    }
}
