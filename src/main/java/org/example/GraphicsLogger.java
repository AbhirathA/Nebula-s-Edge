package org.example;

import java.awt.Color;
import java.util.ArrayList;

public class GraphicsLogger
{
    private final ArrayList<String> list;

    public GraphicsLogger()
    {
        this.list = new ArrayList<>();
    }

    public void logFilledCircle(double x, double y, double radius, Color color)
    {
        list.add("Filled Circle " + x + " " + y + " " + radius + " " + color.getRGB() + "\n");
    }

    public ArrayList<String> getList()
    {
        return list;
    }
}