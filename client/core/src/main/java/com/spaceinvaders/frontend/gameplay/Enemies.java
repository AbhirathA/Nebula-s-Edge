package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Enemies {
    private final Sprite[] enemies;

    private final HashMap<Integer, Integer> entitySpriteMap;

    public Enemies(MyAssetManager assetManager) {
        enemies = new Sprite[12];

        entitySpriteMap = new HashMap<>();

        for(int i=1; i<=12; i++) {
            enemies[i-1] = new Sprite(assetManager.get("textures/enemy" + i, Texture.class));
        }
    }

    public void renderEnemies(Batch batch, ArrayList<Coordinate> coordinateList) {
        for(Coordinate coordinate : coordinateList) {
            if(!entitySpriteMap.containsKey(coordinate.id)) {
                entitySpriteMap.put(coordinate.id, coordinate.id % 12);
            }

            Sprite sprite = enemies[entitySpriteMap.get(coordinate.id)];

            sprite.setPosition(coordinate.x, coordinate.y);
            sprite.setRotation(coordinate.angle);
            sprite.draw(batch);
        }
    }
}
