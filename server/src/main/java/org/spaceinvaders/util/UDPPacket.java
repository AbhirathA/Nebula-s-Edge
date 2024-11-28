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

    public void update(UDPPacket udpPacket) {
        this.id = udpPacket.id;
        this.bullets = udpPacket.bullets;
        this.spaceShips = udpPacket.spaceShips;
        this.asteroids = udpPacket.asteroids;
        this.blackholes = udpPacket.blackholes;
        this.packetNumber = udpPacket.packetNumber;
    }

    public UDPPacket clone() {
        UDPPacket other = new UDPPacket();
        other.id = this.id;
        other.spaceShips = (ArrayList<Coordinate>) this.spaceShips.clone();
        other.asteroids = (ArrayList<Coordinate>) this.asteroids.clone();
        other.bullets = (ArrayList<Coordinate>) this.bullets.clone();
        other.blackholes = (ArrayList<Coordinate>) this.blackholes.clone();
        other.packetNumber = this.packetNumber;


        return other;
    }
}
