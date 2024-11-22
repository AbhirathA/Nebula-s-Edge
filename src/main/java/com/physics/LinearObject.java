package com.physics;

public abstract class LinearObject extends Obj {

    protected LinearObject(long nativeHandle) {
        super(nativeHandle);
    }

    public native double getvX();

    public native double getvY();

    public native void updateV(double x, double y);

    public native double getXacc();

    public native double getYacc();

    public native void updateAcc(double x, double y);

    public native double getOri();

    public native boolean checkXTerminal();

    public native boolean checkYTerminal();

    @Override
    public abstract boolean checkCollision(Obj obj);

    @Override
    public abstract void updatePos(double t);

    @Override
    public abstract double getNextX(double t);

    @Override
    public abstract double getNextY(double t);

    @Override
    public abstract boolean boundCorrection(double lft, double rt, double tp, double bt, double t);

    @Override
    public abstract boolean collisionCorection(Obj other);
}
