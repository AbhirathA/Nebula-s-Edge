package org.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;

public class BlackHole extends Circle
{
    private final double innerRadius;
    
    private final HashSet<Circle> objects;

    public static final double G = 10000;

    public BlackHole(double x, double y, double innerRadius, double radius, double mass)
    {
        super(x, y, radius, mass, 0, 0, 0, 0, Color.BLACK, 1);

        this.innerRadius = innerRadius;
        this.objects = new HashSet<>();
    }

    public void add(Circle circle)
    {
        this.objects.add(circle);
    }

    public void update()
    {
        ArrayList<Circle> toRemove = new ArrayList<>();
        
        for(Circle circle : this.objects)
        {
            double dx = this.getX() - circle.getX();
            double dy = this.getY() - circle.getY();

            double distance = dx * dx + dy * dy;

            if(!(circle instanceof BlackHole) 
                && distance < (this.innerRadius + circle.getRadius()) * (this.innerRadius + circle.getRadius()) - FLOATING_ERROR)
            {
                circle.destroy();
                toRemove.add(circle);
            }
            else if(distance < (this.getRadius() + circle.getRadius()) * (this.getRadius() + circle.getRadius()) - FLOATING_ERROR)
            {
                double [] acceleration = new double[] {dx * G * circle.getMass() / (distance) / (Math.sqrt(distance)), 
                        dy * G * circle.getMass()/ (distance) / (Math.sqrt(distance))};
                
                circle.addAcceleration(acceleration[0], acceleration[1]);
            }
            else
            {
                circle.addAcceleration(0, 0);
                toRemove.add(circle);
            }
        }

        for(Circle circle : toRemove)
        {
            this.objects.remove(circle);
        }
    }

    @Override
    public void draw(final Graphics2D g, GraphicsLogger logger)
    {
        g.setColor(this.getColor());
        g.fillArc((int)(this.getX() - this.innerRadius), (int)(this.getY() - this.innerRadius), (int)(2 * this.innerRadius), (int)(2 * this.innerRadius), 0, 360);
        logger.logFilledCircle(this.getX() - this.innerRadius, this.getY() - this.innerRadius, this.innerRadius, this.getColor());
    } 
}
