package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;

public class Bullets {
    private final Sprite bullet;

    public Bullets(MyAssetManager assetManager) {
        bullet = new Sprite(assetManager.get("textures/bullet.png", Texture.class));
    }

    public void renderBullets(Batch batch, ArrayList<Coordinate> coordinateList) {
        for(Coordinate coordinate : coordinateList) {
            bullet.setPosition(coordinate.x, coordinate.y);
            bullet.setRotation(coordinate.angle);
            bullet.draw(batch);
        }
    }
}
