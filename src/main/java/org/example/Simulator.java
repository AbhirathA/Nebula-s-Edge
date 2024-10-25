package org.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Simulator
{
    private final Timer timer;
    private final ShapeManager shapeManager;
    private final Server server;

    public static final int SAMPLE_TIME_MS = 1;

    public Simulator() 
    {
        SimulationFrame simulationFrame = new SimulationFrame();
        
        shapeManager = new ShapeManager();
        server = new Server();

        BlackHole blackHole = new BlackHole(700, 100, 50, 100, 1000);
        BlackHole blackHole1 = new BlackHole(300, 100, 50, 100, 1000);
        BlackHole blackHole2 = new BlackHole(100, 800, 50, 100, 1000);
        BlackHole blackHole3 = new BlackHole(800, 800, 50, 100, 1000);
        shapeManager.addShape(blackHole);
        shapeManager.addShape(blackHole1);
        shapeManager.addShape(blackHole2);
        shapeManager.addShape(blackHole3);

        Random random = new Random();
        Timer t2 = new Timer(50, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int x = 100;
                int y = 200;
                int radius = 5;
                Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                shapeManager.addShape(new Circle(x, y, radius, radius * radius * radius, 0, 0, 500, 500, color, 1));
            }
        });

        t2.start();

        CustomPanel panel = new CustomPanel();
        simulationFrame.add(panel);

        timer = new Timer(SAMPLE_TIME_MS, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                panel.repaint();
                shapeManager.update(SAMPLE_TIME_MS / 1000.0, panel.getWidth(), panel.getHeight());
            }
        });

        simulationFrame.setVisible(true);
    }

    class CustomPanel extends JPanel 
    {
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);

            GraphicsLogger logger = new GraphicsLogger();
            timer.start();

            shapeManager.draw((Graphics2D)g, logger);
            server.sendDataToClients(logger.getList());
        }
    }

    public static void main(String[] args) 
    {
        new Simulator();
    }
}
