package com.spaceinvaders.backend.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class UDPPacket implements Serializable {

    public Coordinate myShip;
    public ArrayList<Coordinate> spaceShips;
    public ArrayList<Coordinate> asteroids;
    public ArrayList<Coordinate> bullets;
    public long packetNumber;

    public UDPPacket() {
        myShip = new Coordinate("MYSHIP", 0, 0, 0);
        spaceShips = new ArrayList<>();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    public UDPPacket(float x, float y, float angle) {
        myShip = new Coordinate("MYSHIP", x, y, angle);
        spaceShips = new ArrayList<>();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    public void update(UDPPacket udpPacket) {
        this.myShip = udpPacket.myShip;
        this.bullets = udpPacket.bullets;
        this.spaceShips = udpPacket.spaceShips;
        this.asteroids = udpPacket.asteroids;
        this.packetNumber = udpPacket.packetNumber;
    }
}
