// FixedObj.java
package com.physics;

public class FixedObj extends LinearObj {

    public FixedObj(int id, int x, int y, int innerRad, int outerRad, int mass) {
        super(createNativeObject(id, x, y, innerRad, outerRad, mass));
    }

    private static native long createNativeObject(int id, int x, int y, int innerRad, int outerRad, int mass);

    private native void destroyNativeObject(long nativeHandle);

    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }

    // Implement abstract methods using native code
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
