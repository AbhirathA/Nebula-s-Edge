package org.example;

import java.awt.Graphics2D;
import java.awt.Shape;

public interface MovingShape extends Shape, Physics
{
    public abstract void update(double deltaTime, double width, double height);
    public abstract void reverseVelocity();
    public abstract void draw(Graphics2D graphics, GraphicsLogger logger);
    public abstract double getMass();
    public default boolean intersects(MovingShape circle)
    {
        throw new UnsupportedOperationException("Intersection with Circle is not supported.");
    }
}
