package org.example;

import javax.swing.JFrame;

public class SimulationFrame extends JFrame
{
    public static final int FRAME_WIDTH = 1000, FRAME_HEIGHT = 1000;
    
    public SimulationFrame()
    {
        setTitle("Simulator");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }    
}
