package org.spaceinvaders.firebase.util;

import java.io.Serial;
import java.io.Serializable;

public class Coordinate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // For serialization
    public String type; // Coordinate type (e.g., "Point", "Circle")
    public float x, y;

    public Coordinate(String type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", x: " + x + ", y: " + y;
    }
}
