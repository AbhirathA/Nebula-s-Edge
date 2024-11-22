package com.physics;

public abstract class AngleObj extends Obj {

    protected AngleObj(long nativeHandle) {
        super(nativeHandle);
    }

    public AngleObj(int id, double x, double y, double v, double angle, double acc,
            double accX, double accY, double innerRad, double outerRad, double mass) {
        super(createNativeObject(id, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass));
    }

    private static native long createNativeObject(int id, double x, double y, double v, double angle,
            double acc, double accX, double accY,
            double innerRad, double outerRad, double mass);

    private native void destroyNativeObject(long nativeHandle);

    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }

    // Native methods corresponding to C++ methods
    public native double getX();

    public native double getY();

    public native double getV();

    public native void updateV(double v);

    public native void updateX(double x);

    public native void updateY(double y);

    public native double getXacc();

    public native double getYacc();

    public native void updateAcc(double x, double y);

    public native double getAngle();

    public native double getOri();

    public native double getOuterR();

    public native double getInnerR();

    public native int getState();

    public native double getMass();

    public native void changeState();

    // Abstract methods to be implemented in subclasses
    public abstract boolean checkCollision(Obj obj);

    public abstract void updatePos(double t);

    public abstract double getNextX(double t);

    public abstract double getNextY(double t);

    public abstract boolean boundCorrection(double lft, double rt, double tp, double bt, double t);

    public abstract boolean collisionCorection(Obj other);

    static {
        System.loadLibrary("PhysicsEngine");
    }
}
