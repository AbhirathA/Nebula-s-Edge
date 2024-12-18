package com.spaceinvaders.frontend.gameplay;

import com.spaceinvaders.backend.utils.Coordinate;

import java.util.ArrayList;
import java.util.Random;

public class CoordinateTest {
    public static ArrayList<Coordinate> generateCoordinates(int count) {
        Random random = new Random();
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String[] types = {"attack", "health", "bonus"};
            String type = types[random.nextInt(types.length)];
            int id = i;
            float x = random.nextFloat() * 720; // Random x between 0 and 720
            float y = random.nextFloat() * 405; // Random y between 0 and 405
            float angle = random.nextFloat() * 360; // Random angle between 0 and 360 degrees

            coordinates.add(new Coordinate(type, id, (int)x, (int)y, (int)angle));
        }

        return coordinates;
    }
}
