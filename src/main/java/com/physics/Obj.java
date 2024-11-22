// Obj.java
package com.physics;

public abstract class Obj {

    protected long nativeHandle;

    protected Obj(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    // Native methods for common properties
    public native int getX();

    public native int getY();

    public native void updateX(int x);

    public native void updateY(int y);

    public native int getOuterR();

    public native int getInnerR();

    public native int getState();

    public native int getMass();

    public native void changeState();

    // Abstract methods
    public abstract boolean checkCollision(Obj obj);

    public abstract void updatePos(int t);

    public abstract int getNextX(int t);

    public abstract int getNextY(int t);

    public abstract boolean boundCorrection(int lft, int rt, int tp, int bt, int t);

    public abstract boolean collisionCorection(Obj other);

    // Finalizer to clean up native resources
    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }

    private native void destroyNativeObject(long nativeHandle);
}
