package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;

public class Powerups {
    private final Sprite attack;
    private final Sprite health;
    private final Sprite bonus;

    public Powerups(MyAssetManager assetManager) {
        attack = new Sprite(assetManager.get("textures/powerup1.png", Texture.class));
        health = new Sprite(assetManager.get("textures/powerup2.png", Texture.class));
        bonus = new Sprite(assetManager.get("textures/powerup3.png", Texture.class));
    }

    public void renderPowerups(Batch batch, ArrayList<Coordinate> coordinateList) {
        for(Coordinate coordinate : coordinateList) {
            Sprite sprite;
            switch (coordinate.type) {
                case "attack":
                    sprite = attack;
                    break;
                case "health":
                    sprite = health;
                    break;
                case "bonus":
                    sprite = bonus;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid powerup type: " + coordinate.type);
            }

            sprite.setPosition(coordinate.x, coordinate.y);
            sprite.setRotation(coordinate.angle);
            sprite.draw(batch);
        }
    }
}
