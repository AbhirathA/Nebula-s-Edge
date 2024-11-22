package com.physics;

public abstract class Obj {
    protected long nativeHandle;

    protected Obj(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    private native void destroyNativeObject(long nativeHandle);

    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }

    // Native methods
    public native double getX();

    public native double getY();

    public native void updateX(double x);

    public native void updateY(double y);

    public native double getOuterR();

    public native double getInnerR();

    public native double getState();

    public native double getMass();

    public native void changeState();

    // Abstract methods
    public abstract boolean checkCollision(Obj obj);

    public abstract void updatePos(double t);

    public abstract double getNextX(double t);

    public abstract double getNextY(double t);

    public abstract boolean boundCorrection(double lft, double rt, double tp, double bt, double t);

    public abstract boolean collisionCorection(Obj other);
}
