package org.spaceinvaders.util;

import java.io.Serializable;
import java.util.ArrayList;

public class UDPPacket implements Serializable {
    public int id;
    public ArrayList<Coordinate> spaceShips;
    public ArrayList<Coordinate> asteroids;
    public ArrayList<Coordinate> bullets;
    public ArrayList<Coordinate> blackholes;
    public long packetNumber;

    public UDPPacket() {
        spaceShips = new ArrayList<>();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        blackholes = new ArrayList<>();
    }

    public UDPPacket(int id) {
        this.id = id;
    }

    public void update(UDPPacket udpPacket) {
        this.id = udpPacket.id;
        this.bullets = udpPacket.bullets;
        this.spaceShips = udpPacket.spaceShips;
        this.asteroids = udpPacket.asteroids;
        this.blackholes = udpPacket.blackholes;
        this.packetNumber = udpPacket.packetNumber;
    }

    public UDPPacket clone() {
        try {
            return (UDPPacket) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new UDPPacket(this.id);
    }
}
