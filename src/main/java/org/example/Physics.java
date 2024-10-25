package org.example;

public interface Physics
{
    public abstract void update(double deltaTime, double [] acceleration);
    public abstract double[] getVelocity();
    public abstract double[] getPosition();
}