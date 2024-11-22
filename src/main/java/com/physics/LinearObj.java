// LinearObj.java
package com.physics;

public abstract class LinearObj extends Obj {

    protected LinearObj(long nativeHandle) {
        super(nativeHandle);
    }

    // Native methods specific to LinearObj
    public native int getvX();

    public native int getvY();

    public native void updateV(int x, int y);

    public native int getXacc();

    public native int getYacc();

    public native void updateAcc(int x, int y);

    public native double getOri();

    public native boolean checkXTerminal();

    public native boolean checkYTerminal();

    // Abstract methods inherited from Obj
    @Override
    public abstract boolean checkCollision(Obj obj);

    @Override
    public abstract void updatePos(int t);

    @Override
    public abstract int getNextX(int t);

    @Override
    public abstract int getNextY(int t);

    @Override
    public abstract boolean boundCorrection(int lft, int rt, int tp, int bt, int t);

    @Override
    public abstract boolean collisionCorection(Obj other);
}
