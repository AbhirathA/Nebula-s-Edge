package com.spaceinvaders.backend.utils;

import com.spaceinvaders.frontend.SpaceInvadersGame;

import java.io.Serial;
import java.io.Serializable;

public class Coordinate implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L; // For serialization
    public String type; // Coordinate type (e.g., "Point", "Circle")
    public int x, y, angle, point, health;
    public int id;

    public Coordinate(String type, int id, int x, int y, int angle, int point, int health)
    {
        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.point = point;
        this.health = health;
    }

    public Coordinate(String type, int id, int x, int y, int angle)
    {
        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.point = 0;
        this.health = 0;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", x: " + x + ", y: " + y;
    }

    public float getX() { return this.x / 10f; }

    public float getY() { return (this.y + SpaceInvadersGame.GAME_HEIGHT * 10) / 10f; }

    public float getAngle() {System.out.println(this.angle); return this.angle / 10f - 90;  }

    public int getId() { return this.id;}
}
