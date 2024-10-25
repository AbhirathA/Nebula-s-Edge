package org.example;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class ShapeManager
{
    private final ArrayList<Circle> shapes;
    
    public ShapeManager()
    {
        this.shapes = new ArrayList<>();
    }

    public void addShape(Circle shape)
    {
        shapes.add(shape);
    }

    public void update(double deltaTime, int width, int height)
    {
        for (Circle circle : shapes)
        {
            if (circle instanceof BlackHole)
            {
                ((BlackHole) circle).update();
            }
        }

        for(int i = 0; i < shapes.size(); i++)
        {
            if(shapes.get(i).getState() == State.DESTROYED)
            {
                shapes.remove(i);
                i--;
            }
        }

        for (Circle circle : shapes)
        {
            circle.update(deltaTime, width, height);
        }

        ArrayList<Pair> list = new ArrayList<>();

        for(Circle shape : shapes)
        {
            list.add(new Pair(shape, (int)(Math.floor(shape.getBounds2D().getMinX())), true));
            list.add(new Pair(shape, (int)(Math.ceil(shape.getBounds2D().getMaxX())), false));
        }

        list.sort(new PairComparator());

        ArrayList<Pair> collidingShapes = new ArrayList<>();
        HashSet<IntersectingCircles> updatedShapes = new HashSet<>();

        for(Pair pair : list)
        {
            if(!pair.isStart())
                collidingShapes.remove(pair);
            else
            {
                for(Pair p : collidingShapes)
                {
                    if(p != pair && p.getShape() instanceof BlackHole && !(pair.getShape() instanceof BlackHole) 
                        && pair.getShape().intersects(p.getShape()))
                    {
                        ((BlackHole)(p.getShape())).add(pair.getShape());
                    }
                    else if(p != pair && pair.getShape() instanceof BlackHole && !(p.getShape() instanceof BlackHole) 
                        && pair.getShape().intersects(p.getShape()))
                    {
                        ((BlackHole)(pair.getShape())).add(p.getShape());
                    }
                    else if(p != pair && p.getShape().intersects(pair.getShape()))
                    {
                        performReversal(pair, p);
                        updatedShapes.add(new IntersectingCircles(pair.getShape(), p.getShape()));
                    }
                }
                collidingShapes.add(pair);
            }
        }

        for(IntersectingCircles circles : updatedShapes)
        {
            separateCircles(circles.getA(), circles.getB());
        }
    }

    private void separateCircles(Circle a, Circle b) 
    {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double overlap = a.getRadius() + b.getRadius() - distance;
        
        if (overlap > 0) 
        {
            double adjustmentFactor = overlap / 2.0;
            double adjustmentX = (dx / distance) * adjustmentFactor;
            double adjustmentY = (dy / distance) * adjustmentFactor;
            
            a.setCenter(a.getX() - adjustmentX, a.getY() - adjustmentY);
            b.setCenter(b.getX() + adjustmentX, b.getY() + adjustmentY);
        }
    }

    private void performReversal(Pair p1, Pair p2) 
    {
        double x1 = p1.getShape().getX(), y1 = p1.getShape().getY();
        double x2 = p2.getShape().getX(), y2 = p2.getShape().getY();
        
        double dx = x2 - x1;
        double dy = y2 - y1;
        
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        double nx = dx / distance;
        double ny = dy / distance;
        
        double v1x = p1.getShape().getXVelocity(), v1y = p1.getShape().getYVelocity();
        double v2x = p2.getShape().getXVelocity(), v2y = p2.getShape().getYVelocity();
        
        double m1 = p1.getShape().getMass();
        double m2 = p2.getShape().getMass();
        
        double v1Along = v1x * nx + v1y * ny;
        double v2Along = v2x * nx + v2y * ny;
        
        double v1PerpX = v1x - v1Along * nx;
        double v1PerpY = v1y - v1Along * ny;
        double v2PerpX = v2x - v2Along * nx;
        double v2PerpY = v2y - v2Along * ny;
        
        double v1NewAlong = ((m1 - m2) * v1Along + 2 * m2 * v2Along) / (m1 + m2);
        double v2NewAlong = ((m2 - m1) * v2Along + 2 * m1 * v1Along) / (m1 + m2);
        
        v1x = v1NewAlong * nx + v1PerpX;
        v1y = v1NewAlong * ny + v1PerpY;
        
        v2x = v2NewAlong * nx + v2PerpX;
        v2y = v2NewAlong * ny + v2PerpY;
        
        p1.getShape().setXVelocity(v1x);
        p1.getShape().setYVelocity(v1y);
        p2.getShape().setXVelocity(v2x);
        p2.getShape().setYVelocity(v2y);
    }    

    public void draw(Graphics2D g, GraphicsLogger logger)
    {
        for(Circle shape : shapes)
        {
            shape.draw(g, logger);
        }
    }

    public boolean intersects(Circle s)
    {
        for(Circle shape : shapes)
        {
            if(shape.intersects(s))
                return true;
        }
        return false;

    }

    private static class Pair
    {
        private final Circle shape;
        private final int x;
        private final boolean isStart;

        public Pair(Circle shape, int x, boolean isStart)
        {
            this.shape = shape;
            this.x = x;
            this.isStart = isStart;
        }

        public Circle getShape()
        {
            return shape;
        }

        public int getX()
        {
            return x;
        }

        public boolean isStart()
        {
            return isStart;
        }
    }

    private final static class PairComparator implements Comparator<Pair>
    {
        @Override
        public int compare(Pair p1, Pair p2) 
        {
            return p1.getX() - p2.getX();
        }
    }

    private final static class IntersectingCircles
    {
        private final Circle a, b;

        public IntersectingCircles(Circle a, Circle b)
        {
            this.a = a;
            this.b = b;
        }

        public Circle getA()
        {
            return a;
        }

        public Circle getB()
        {
            return b;
        }
    }
}
