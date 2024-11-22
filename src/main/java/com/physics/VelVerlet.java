package com.physics

public class VelVerlet extends LinearObject {

    public VelVerlet(int id, double x, double y, double vX, double vY, double accX, double accY,
            double res, double innerRad, double outerRad, double mass) {
        super(createNativeObject(id, x, y, vX, vY, accX, accY, res, innerRad, outerRad, mass));
    }

    private static native long createNativeObject(int id, double x, double y, double vX, double vY,
            double accX, double accY, double res,
            double innerRad, double outerRad, double mass);

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

    static {
        System.loadLibrary("PhysicsEngine");
    }
}
