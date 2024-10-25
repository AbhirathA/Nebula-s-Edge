package org.example;

public class Physics2D implements Physics
{
    private final Physics1D x, y;

    public Physics2D(double x, double y)
    {
        this.x = new Physics1D(x);
        this.y = new Physics1D(y);
    }

    public void update(double deltaTime, double [] acceleration)
    {
        if(acceleration.length < 2) throw new IllegalArgumentException("Acceleration must be of length 2");

        this.x.update(deltaTime, new double [] {acceleration[0]});
        this.y.update(deltaTime, new double [] {acceleration[1]});
    }

    public double[] getVelocity()
    {
        return new double[]{this.x.getParticleVelocity(), this.y.getParticleVelocity()};
    }

    public double[] getPosition()
    {
        return new double[]{this.x.getPos(), this.y.getPos()};
    }

    public double getX()
    {
        return this.x.getPos();
    }

    public void setX(double x)
    {
        this.x.setPos(x);
    }

    public double getXVelocity()
    {
        return this.x.getParticleVelocity();
    }

    public void setXVelocity(double velocity)
    {
        this.x.setVelocity(velocity);
    }

    public double getXAcceleration()
    {
        return this.x.getAcceleration();
    }

    public double getY()
    {
        return this.y.getPos();
    }

    public void setY(double y)
    {
        this.y.setPos(y);
    }

    public double getYVelocity()
    {
        return this.y.getParticleVelocity();
    }

    public void setYVelocity(double velocity)
    {
        this.y.setVelocity(velocity);
    }

    public double getYAcceleration()
    {
        return this.y.getAcceleration();
    }

    public double getNextXPos(double deltaTime)
    {
        return this.x.getNextPos(deltaTime);
    }

    public double getNextYPos(double deltaTime)
    {
        return this.y.getNextPos(deltaTime);
    }
}