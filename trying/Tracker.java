// Tracker.java
package com.physics;

public class Tracker extends LinearObject {

    public Tracker(int id, int x, int y, int v, int res, int innerRad, int outerRad, int mass, boolean startX,
            int startSign, Obj aim) {
        super(createNativeObject(id, x, y, v, res, innerRad, outerRad, mass, startX, startSign, aim.nativeHandle));
    }

    private static native long createNativeObject(int id, int x, int y, int v, int res, int innerRad, int outerRad,
            int mass, boolean startX, int startSign, long aimHandle);

    private native void destroyNativeObject(long nativeHandle);

    // Implement abstract methods
    @Override
    public native boolean checkCollision(Obj obj);

    @Override
    public native void updatePos(int t);

    @Override
    public native int getNextX(int t);

    @Override
    public native int getNextY(int t);

    @Override
    public native boolean boundCorrection(int lft, int rt, int tp, int bt, int t);

    @Override
    public native boolean collisionCorection(Obj other);
}
