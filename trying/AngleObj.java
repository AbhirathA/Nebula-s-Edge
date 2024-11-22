package com.physics;

public abstract class AngleObj extends Obj {

    protected AngleObj(long nativeHandle) {
        super(nativeHandle);
    }

    public AngleObj(int id, int x, int y, int v, int angle, int acc,
            int accX, int accY, int innerRad, int outerRad, int mass) {
        super(createNativeObject(id, x, y, v, angle, acc, accX, accY, innerRad, outerRad, mass));
    }

    private static native long createNativeObject(int id, int x, int y, int v, int angle,
            int acc, int accX, int accY,
            int innerRad, int outerRad, int mass);

    private native void destroyNativeObject(long nativeHandle);

    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }

    // Native methods corresponding to C++ methods

    public native int getV();

    public native void updateV(int v);

    public native void updateX(int x);

    public native void updateY(int y);

    public native int getXacc();

    public native int getYacc();

    public native void updateAcc(int x, int y);

    public native int getAngle();

    public native int getOri();

    public native void changeState();

    // Abstract methods to be implemented in subclasses
    public abstract boolean checkCollision(Obj obj);

    public abstract void updatePos(int t);

    public abstract int getNextX(int t);

    public abstract int getNextY(int t);

    public abstract boolean boundCorrection(int lft, int rt, int tp, int bt, int t);

    public abstract boolean collisionCorection(Obj other);

    static {
        System.loadLibrary("PhysicsEngine");
    }
}
