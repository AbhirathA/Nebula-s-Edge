// src/main/java/com/physics/FixedObj.java
package com.physics;

public class FixedObj extends LinearObject {

    public FixedObj(int id, double x, double y, double innerRad, double outerRad, double mass) {
        super(createNativeObject(id, x, y, innerRad, outerRad, mass));
    }

    private static native long createNativeObject(int id, double x, double y, double innerRad, double outerRad,
            double mass);

    private native void destroyNativeObject(long nativeHandle);

    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }

    @Override
    public native void updatePos(double t);

    @Override
    public native double getNextX(double t);

    @Override
    public native double getNextY(double t);

    @Override
    public native boolean checkCollision(Obj obj);

    @Override
    public native boolean boundCorrection(double lft, double rt, double tp, double bt, double t);

    @Override
    public native boolean collisionCorection(Obj other);
}
