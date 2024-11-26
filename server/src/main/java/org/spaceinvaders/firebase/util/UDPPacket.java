package org.spaceinvaders.firebase.util;

import java.io.Serializable;
import java.util.ArrayList;

public class UDPPacket implements Serializable {
    public Coordinate myShip;
    public ArrayList<Coordinate> spaceShips;
    public ArrayList<Coordinate> asteroids;
    public ArrayList<Coordinate> bullets;
    public ArrayList<Coordinate> blackholes;
    public long packetNumber;

    public UDPPacket() {
        myShip = new Coordinate("MYSHIP", 0, 0, 0);
        spaceShips = new ArrayList<>();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        blackholes = new ArrayList<>();
    }

    public UDPPacket(float x, float y, float angle) {
        myShip = new Coordinate("MYSHIP", x, y, angle);
        spaceShips = new ArrayList<>();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        blackholes = new ArrayList<>();
    }

    // this is incorrect
    // TODO: need to change server to handle multiple clients
    public UDPPacket(Coordinate myShip) {
        this.myShip = myShip;
        spaceShips = new ArrayList<>();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
        blackholes = new ArrayList<>();
    }

    public void update(UDPPacket udpPacket) {
        this.myShip = udpPacket.myShip;
        this.bullets = udpPacket.bullets;
        this.spaceShips = udpPacket.spaceShips;
        this.asteroids = udpPacket.asteroids;
        this.blackholes = udpPacket.blackholes;
        this.packetNumber = udpPacket.packetNumber;
    }
}