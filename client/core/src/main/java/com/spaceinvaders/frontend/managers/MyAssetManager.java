package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MyAssetManager {
    // The AssetManager handles all asset loading and management
    private final AssetManager assetManager;

    // Constructor initializes the AssetManager instance
    public MyAssetManager() {
        this.assetManager = new AssetManager();
    }

    // Loads all game assets (textures, sounds, fonts) into the AssetManager
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
        assetManager.load("textures/menu.png", Texture.class);
        assetManager.load("textures/music.png", Texture.class);
        assetManager.load("textures/play.png", Texture.class);
        assetManager.load("textures/restart.png", Texture.class);
        assetManager.load("textures/sound.png", Texture.class);
        assetManager.load("textures/sliderBackground.png", Texture.class);
        assetManager.load("textures/sliderKnob.png", Texture.class);
        assetManager.load("sounds/mixkit-winning-a-coin-video-game-2069.wav", Sound.class);
        assetManager.load("sounds/mixkit-short-laser-gun-shot-1670.wav", Sound.class);
        assetManager.load("textures/Rocket3.png", Texture.class);
        assetManager.load("textures/heart.png", Texture.class);
        assetManager.load("textures/halfHeart.png", Texture.class);
        assetManager.load("textures/emptyHeart.png", Texture.class);
    }

    // Retrieves an asset from the AssetManager based on its file path and type
    public <T> T get(String filePath, Class<T> type) {
        return assetManager.get(filePath, type);
    }

    // Disposes of all assets in the AssetManager to free resources
    public void dispose() {
        assetManager.dispose();
    }

    // Updates the AssetManager, returns true when all assets are loaded
    public boolean update() {
        return assetManager.update();
    }

    // Returns the progress of asset loading (from 0.0f to 1.0f)
    public float getProgress() {
        return assetManager.getProgress();
    }

    // Checks if a specific asset has been loaded
    public boolean isAssetLoaded(String path) {
        return assetManager.isLoaded(path);
    }
}
