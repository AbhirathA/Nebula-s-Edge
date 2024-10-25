package org.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Circle extends Physics2D implements MovingShape
{
    private final double radius, mass, accX, accY;
    private double addAccX, addAccY;
    private final Color color;
    private State state;

    public static final double FLOATING_ERROR = 1e-7;
    public final double COEFF_OF_RESTITUTION;

    public Circle(double x, double y, double radius, double mass, double accX, double accY, double xVelocity, double yVelocity, Color color, final double COEFF_OF_RESTITUTION)
    {
        super(x, y);

        this.radius = radius;
        this.mass = mass;
        this.accX = accX;
        this.accY = accY;
        
        this.addAccX = 0;
        this.addAccY = 0;

        this.setXVelocity(xVelocity);
        this.setYVelocity(yVelocity);

        this.color = color;
        this.state = State.ALIVE;

        this.COEFF_OF_RESTITUTION = COEFF_OF_RESTITUTION;
    }

    @Override
    public boolean contains(final Point2D p) 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.contains(p);
    }

    @Override
    public boolean contains(final Rectangle2D r) 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.contains(r);
    }

    @Override
    public boolean contains(double x, double y) 
    {
        return contains(new Point2D.Double(x, y));
    }

    @Override
    public boolean contains(double x, double y, double w, double h) 
    {
        return contains(new Rectangle2D.Double(x, y, w, h));
    }

    @Override
    public Rectangle getBounds() 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.getBounds2D();
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at) 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at, double flatness) 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.getPathIterator(at, flatness);
    }

    @Override
    public boolean intersects(final Rectangle2D r) 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.intersects(r);
    }

    public boolean intersects(final Circle circle)
    {
        double dx = Math.abs(this.getX() - circle.getX());
        double dy = Math.abs(this.getY() - circle.getY());

        double distance = dx * dx + dy * dy;

        return distance < (this.radius + circle.radius) * (this.radius + circle.radius) - FLOATING_ERROR;
    }

    @Override
    public boolean intersects(final MovingShape other)
    {
        if(other instanceof Circle)
            return this.intersects((Circle) other);

        return this.intersects(other.getBounds2D());
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) 
    {
        Ellipse2D.Double circle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        return circle.intersects(x, y, w, h);
    }   

    @Override
    public void update(double deltaTime, double width, double height)
    {
        super.update(deltaTime, new double[] {accX + addAccX, accY + addAccY});

        if ((this.getNextXPos(deltaTime) + radius >= width && this.getXVelocity() > 0) ||
            (this.getNextXPos(deltaTime) - radius <= 0 && this.getXVelocity() < 0)) 
        {
            this.setXVelocity(COEFF_OF_RESTITUTION * -this.getXVelocity());
            separate(width, height);
        }

        if ((this.getNextYPos(deltaTime) + radius >= height && this.getYVelocity() > 0) ||
            (this.getNextYPos(deltaTime) - radius <= 0 && this.getYVelocity() < 0)) 
        {
            this.setYVelocity(COEFF_OF_RESTITUTION * -this.getYVelocity());
            separate(width, height);
        }
    }

    private void separate(double width, double height) 
    {
        if(this.getX() - radius < 0)
            this.setX(this.radius);
        if(this.getY() - radius < 0)
            this.setY(this.radius);
        if(this.getX() + radius > width)
            this.setX(width - radius);
        if(this.getY() + radius > height)
            this.setY(height - radius);
    }

    @Override
    public void update(double deltaTime, double [] acceleration)
    {
        throw new UnsupportedOperationException("Cannot update the velocity using this method.");
    }

    public void setCenter(double x, double y)
    {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void draw(final Graphics2D g, GraphicsLogger logger)
    {
        g.setColor(this.color);
        g.fill(this);
        logger.logFilledCircle(this.getX() - this.radius, this.getY() - this.radius, this.radius, color);
    }

    @Override
    public void reverseVelocity()
    {
        this.setXVelocity(-this.getXVelocity());
        this.setYVelocity(-this.getYVelocity());
    }

    public double getRadius()
    {
        return this.radius;
    }

    @Override
    public double getMass()
    {
        return this.mass;
    }

    public Color getColor()
    {
        return this.color;
    }

    @Override
    public String toString()
    {
        return this.color.toString();
    }

    public State getState()
    {
        return this.state;
    }

    public void destroy()
    {
        this.state = State.DESTROYED;
    }

    public void addAcceleration(double accX, double accY)
    {
        this.addAccX = accX;
        this.addAccY = accY;
    }
}