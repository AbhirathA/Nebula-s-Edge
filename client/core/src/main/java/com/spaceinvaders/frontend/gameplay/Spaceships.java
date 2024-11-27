package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Spaceships {
    private final Sprite[] spaceships;

    private final HashMap<Integer, Integer> entitySpriteMap;

    public Spaceships(MyAssetManager assetManager) {
        spaceships = new Sprite[3];

        entitySpriteMap = new HashMap<>();

        for(int i=1; i<=3; i++) {
            spaceships[i-1] = new Sprite(assetManager.get("textures/Rocket" + i + ".png", Texture.class));
        }
    }

    public void renderSpaceships(Batch batch, ArrayList<Coordinate> coordinateList, int id) {
        for(Coordinate coordinate : coordinateList) {
            if(coordinate.id == id) {
                break;
            }

            if(!entitySpriteMap.containsKey(coordinate.id)) {
                entitySpriteMap.put(coordinate.id, coordinate.id % 3);
            }

            Sprite sprite = spaceships[entitySpriteMap.get(coordinate.id)];
            System.out.println(coordinate.id);
            sprite.setPosition(coordinate.x, coordinate.y);
            sprite.setRotation(coordinate.angle);
            sprite.draw(batch);
        }
    }
}
