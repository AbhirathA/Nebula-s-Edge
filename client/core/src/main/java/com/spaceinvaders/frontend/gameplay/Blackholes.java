package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class Blackholes {
    private final Sprite blackhole;

    public Blackholes(MyAssetManager assetManager) {
        blackhole = new Sprite(assetManager.get("textures/enemy1.png", Texture.class));
    }
}
