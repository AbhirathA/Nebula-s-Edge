package com.physics

public class StdVerlet {

    public native void updatePos(double t);

    public native double getNextX(double t);

    public native double getNextY(double t);

    public native boolean checkCollision(Object obj);

    public native boolean boundCorrection(double lft, double rt, double tp, double bt, double t);

    public native boolean collisionCorrection(Object other);

    public native void init(int id, double x, double y, double vX, double vY, double accX, double accY,
            double res, double innerRad, double outerRad, double mass);

    static {
        System.loadLibrary("PhysicsEngine");
    }
}
