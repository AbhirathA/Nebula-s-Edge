package org.spaceinvaders.util;

import java.io.Serial;
import java.io.Serializable;

public class Coordinate implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L; // For serialization
    public String type; // Coordinate type (e.g., "Point", "Circle")
    public int x, y, angle;
    public int id;

    public Coordinate(String type, int id, int x, int y, int angle)
    {
        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", x: " + x + ", y: " + y;
    }
}
