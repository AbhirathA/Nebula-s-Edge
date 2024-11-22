// velVerlet.java
package com.physics;

public class velVerlet extends LinearObject {

    public velVerlet(int id, int x, int y, int vX, int vY, int accX, int accY, int res, int innerRad, int outerRad,
            int mass) {
        super(createNativeObject(id, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass));
    }

    private static native long createNativeObject(int id, int x, int y, int vX, int vY, int accX, int accY, int res,
            int innerRad, int outerRad, int mass);

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
