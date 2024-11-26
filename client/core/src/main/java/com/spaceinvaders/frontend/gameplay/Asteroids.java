package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Asteroids {
    private final Sprite[] bigAsteroids;
    private final Sprite[] mediumAsteroids;
    private final Sprite[] smallAsteroids;

    private final HashMap<Integer, Integer> entitySpriteMap;

    public Asteroids(MyAssetManager assetManager) {
        bigAsteroids = new Sprite[7];
        mediumAsteroids = new Sprite[7];
        smallAsteroids = new Sprite[7];

        entitySpriteMap = new HashMap<>();

        for(int i=1; i<=7; i++) {
            bigAsteroids[i-1] = new Sprite(assetManager.get("textures/bigAst" + i + ".png", Texture.class));
        }

        for(int i=1; i<=7; i++) {
            mediumAsteroids[i-1] = new Sprite(assetManager.get("textures/midAst" + i + ".png", Texture.class));
        }

        for(int i=1; i<=7; i++) {
            smallAsteroids[i-1] = new Sprite(assetManager.get("textures/smallAst" + i + ".png", Texture.class));
        }
    }

    public void renderAsteroids(Batch batch, ArrayList<Coordinate> coordinateList) {
        for(Coordinate coordinate : coordinateList) {
            if(!entitySpriteMap.containsKey(coordinate.id)) {
                entitySpriteMap.put(coordinate.id, coordinate.id % 7);
            }

            Sprite sprite;
            switch (coordinate.type) {
                case "B":
                    sprite = bigAsteroids[entitySpriteMap.get(coordinate.id)];
                    break;
                case "M":
                    sprite = mediumAsteroids[entitySpriteMap.get(coordinate.id)];
                    break;
                case "S":
                    sprite = smallAsteroids[entitySpriteMap.get(coordinate.id)];
                    break;
                default:
                    throw new IllegalArgumentException("Invalid asteroid type: " + coordinate.type);
            }

            sprite.setPosition(coordinate.x, coordinate.y);
            sprite.setRotation(coordinate.angle);
            sprite.draw(batch);
        }
    }
}
