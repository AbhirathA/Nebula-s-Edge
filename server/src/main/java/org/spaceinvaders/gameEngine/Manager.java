package org.spaceinvaders.gameEngine;

public class Manager {

    static {
//        System.loadLibrary("PhysicsEngine");
        System.load("/home/krishna/Projects/Nebula-s-Edge/server/src/main/java/org/spaceinvaders/gameEngine/lib/libPhysicsEngine.so");

    }

    private long nativeHandle;

    public Manager(int accX, int accY, int lft, int rt, int tp, int bt, int t) {
        this.nativeHandle = createNativeObject(accX, accY, lft, rt, tp, bt, t);
    }

    private native long createNativeObject(int accX, int accY, int lft, int rt, int tp, int bt, int t);

    private native void destroyNativeObject(long nativeHandle);

    public native void update();

    // public native int drop1(int x, int y, int vX, int vY, int accX, int accY, int
    // res, int innerRad, int outerRad,
    // int mass);
    public native int drop1(int x, int y, int v, int angle, int acc, int accX, int accY, int innerRad, int outerRad,
            int mass);

    public native int drop2(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad, int mass);

    public native void removeDead(int[] ids);

    public native int[][] display(int lowerX, int lowerY, int upperX, int upperY);

    @Override
    protected void finalize() throws Throwable {
        destroyNativeObject(nativeHandle);
        super.finalize();
    }
}
