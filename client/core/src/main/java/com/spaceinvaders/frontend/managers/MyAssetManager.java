package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MyAssetManager {
    private final AssetManager assetManager;

    public MyAssetManager() {
        this.assetManager = new AssetManager();
    }

    public void loadAssets() {
        assetManager.load("textures/title.png", Texture.class);
        assetManager.load("textures/Planet1.png", Texture.class);
        assetManager.load("textures/Planet2.png", Texture.class);
        assetManager.load("textures/Planet3.png", Texture.class);
        assetManager.load("textures/Planet4.png", Texture.class);
        assetManager.load("textures/Planet5.png", Texture.class);
        assetManager.load("textures/Planet6.png", Texture.class);
        assetManager.load("textures/Planet7.png", Texture.class);
        assetManager.load("textures/Planet8.png", Texture.class);
        assetManager.load("textures/Planet9.png", Texture.class);
        assetManager.load("textures/Planet10.png", Texture.class);
        assetManager.load("textures/Planet11.png", Texture.class);
        assetManager.load("textures/Planet12.png", Texture.class);
        assetManager.load("textures/Planet13.png", Texture.class);
        assetManager.load("textures/Planet14.png", Texture.class);
        assetManager.load("textures/button.png", Texture.class);
        assetManager.load("textures/back-button.png", Texture.class);
        assetManager.load("textures/outline.png", Texture.class);
        assetManager.load("textures/cursor.png", Texture.class);
        assetManager.load("textures/selection.png", Texture.class);
        assetManager.load("fonts/minecraft.fnt", BitmapFont.class);
    }

    public <T> T get(String filePath, Class<T> type) {
        return assetManager.get(filePath, type);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public boolean update() {
        return assetManager.update();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public boolean isAssetLoaded(String path) {
        return assetManager.isLoaded(path);
    }
}
