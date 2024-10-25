package org.example;

import java.awt.Color;
import java.awt.Graphics;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Client 
{
    private final ShapePanel shapePanel;
    private static final String IP_ADDRESS = "172.16.232.8";
    private static final int PORT = 12345;

    public Client() 
    {
        shapePanel = new ShapePanel();

        SimulationFrame simulationFrame = new SimulationFrame();
        simulationFrame.add(shapePanel);
        simulationFrame.setVisible(true);

        // Start the socket connection and reading data in a separate thread
        new Thread(this::startClient).start();
    }

    private void startClient() 
    {
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) 
        {

            while (true) 
            {
                // Read the DataPacket object from the server
                DataPacket dataPacket = (DataPacket) in.readObject();

                // Update the shapes in the panel with the new data
                SwingUtilities.invokeLater(() -> shapePanel.updateShapes(dataPacket.getDataList()));
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Server Disconnected");
            System.exit(0);
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(Client::new);
    }
}

enum TYPE 
{
    FILLED("Filled"), 
    DRAWN("Drawn");

    private final String type;

    private TYPE(String type)
    {
        this.type = type;
    }

    public static TYPE fromString(String type) 
    {
        for (TYPE t : TYPE.values()) 
        {
            if (t.type.equalsIgnoreCase(type)) 
            {
                return t;
            }
        }
        throw new IllegalArgumentException("No enum constant " + type);
    }
}

class ShapePanel extends JPanel 
{
    private final ArrayList<ShapeData> shapes = new ArrayList<>();

    public void updateShapes(ArrayList<String> dataList) 
    {
        shapes.clear(); // Clear previous shapes

        // Parse data list and create shapes
        for (String data : dataList) 
        {
            String[] parts = data.split(" ");
            String shapeType = parts[0]; // Filled Circle or Drawn Circle
            double x = Double.parseDouble(parts[2].trim());
            double y = Double.parseDouble(parts[3].trim());
            double radius = Double.parseDouble(parts[4].trim());
            Color color = new Color(Integer.parseInt(parts[5].trim()));

            shapes.add(new ShapeData(shapeType, x, y, radius, color));
        }

        repaint(); // Trigger a repaint to update the drawing
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        // Draw each shape
        for (ShapeData shape : shapes) 
        {
            g.setColor(shape.getColor());
            if (shape.getType().equals(TYPE.FILLED)) 
            {
                g.fillOval((int) shape.getX(), (int) shape.getY(), 2 * (int) shape.getRadius(), 2 * (int) shape.getRadius());
            } 
            else if (shape.getType().equals(TYPE.DRAWN)) 
            {
                g.drawOval((int) shape.getX(), (int) shape.getY(), 2 * (int) shape.getRadius(), 2 * (int) shape.getRadius());
            }
        }
    }

    private static class ShapeData 
    {
        private final TYPE type;
        private final double x, y, radius;
        private final Color color;

        public ShapeData(String type, double x, double y, double radius, Color color) 
        {
            this.type = TYPE.fromString(type);
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        public TYPE getType() 
        {
            return type;
        }

        public double getX() 
        {
            return x;
        }

        public double getY() 
        {
            return y;
        }

        public double getRadius() 
        {
            return radius;
        }

        public Color getColor() 
        {
            return color;
        }
    }
}
