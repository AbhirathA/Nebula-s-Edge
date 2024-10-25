package org.example;

public class Physics1D implements Physics
{
    private double pos, velocity, acceleration;

    public Physics1D(double pos)
    {
        this.pos = pos;
        this.velocity = 0;
        this.acceleration = 0;
    }

    public void update(double deltaTime, double [] acceleration)
    {
        if(acceleration.length == 0)
            throw new IllegalArgumentException("No acceleration provided");
        
        this.pos = getNextPos(deltaTime);
        this.velocity = getNextVelocity(deltaTime, acceleration[0]);
        this.acceleration = acceleration[0];
    }

    public double[] getVelocity()
    {
        return new double[]{this.velocity};
    }

    public double[] getPosition()
    {
        return new double[]{this.pos};
    }

    public double getNextPos(double deltaTime)
    {
        return  this.pos + this.velocity * deltaTime + 0.5 * this.acceleration * deltaTime * deltaTime;
    }

    public double getNextVelocity(double deltaTime, double acceleration)
    {
        return this.velocity + 0.5 * (this.acceleration + acceleration) * deltaTime + 0.5 * this.acceleration * deltaTime;
    }

    public void setVelocity(double velocity)
    {
        this.velocity = velocity;
    }

    public double getPos()
    {
        return this.pos;
    }

    public void setPos(double pos)
    {
        this.pos = pos;
    }

    public double getParticleVelocity()
    {
        return this.velocity;
    }

    public double getAcceleration()
    {
        return this.acceleration;
    }
}