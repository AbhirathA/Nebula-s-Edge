package com.physics;

public class Manager {

        static {
                System.loadLibrary("PhysicsEngine");
        }

        private long nativeHandle;

        public Manager(int accX, int accY, int lft, int rt, int tp, int bt, int t) {
                this.nativeHandle = createNativeObject(accX, accY, lft, rt, tp, bt, t);
        }

        private native long createNativeObject(int accX, int accY, int lft, int rt, int tp, int bt, int t);

        private native void destroyNativeObject(long nativeHandle);

        public native void update();

        public native int dropUser(int x, int y, int peakV, int driftV, int angle, int thrust, int thrustPersistance,
                        int movePersistance, int coolDown, int accX, int accY, int innerRad, int outerRad, int mass,
                        int health, int bulletSpeed, int bulletLife);

        public native int shoot(int id, int innerRadius, int outerRadius, int mass);

        public native int dropAsteroid(int x, int y, int innerRad, int outerRad, int mass);

        public native int dropBlackHole(int x, int y, int innerRad, int outerRad, int mass);

        public native int dropEnemy(int x, int y, int v, int res, int innerRad, int outerRad, int mass, boolean startX,
                        int startSign, int aim);

        public native int dropMeteor(int x, int y, int vX, int vY, int accX, int accY, int innerRad, int outerRad,
                        int mass);

        public native int dropHealthPowerUp(int x, int y, int radius, int healthIncrease);

        public native int dropBulletBoostPowerUp(int x, int y, int radius, int speedBoost, int lifeBoost, int duration);

        public native int dropIncreasePointsPowerUp(int x, int y, int radius, int pointScale, int duration);

        public native int getHealth(int id);

        public native int getPoints(int id);

        public native int[][] display(int lowerX, int lowerY, int upperX, int upperY);

        public native void forward(int id);

        public native void stop(int id);

        public native void thrust(int id);

        public native void left(int id);

        public native void right(int id);

        public native void removeDead(int[] ids);

        /**
         * Destroys an object (e.g. the ship of a player who left). It disappears on the next update.
         */
        public native void remove(int id);

        /**
         * Frees the native world. Call it from the thread that ran the simulation,
         * because the engine keeps its timers per thread. Calls made after this are no-ops.
         */
        public synchronized void dispose() {
                if (nativeHandle != 0) {
                        destroyNativeObject(nativeHandle);
                        nativeHandle = 0;
                }
        }
}
