package com.spaceinvaders.backend.utils;

import java.io.Serial;
import java.io.Serializable;

public class Coordinate implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L; // For serialization
    public String type; // Coordinate type (e.g., "Point", "Circle")
    public float x, y, angle;
    public int id;

    public Coordinate(String type, int id, float x, float y, float angle)
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

    public float getX() { return (this.x + 4050) / 10f; }

    public float getY() { return this.y / 10f; }

    public float getAngle() { return this.angle / 10f; }

    public int getId() { return this.id;}
}
