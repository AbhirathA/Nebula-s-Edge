package com.spaceinvaders.frontend.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class PlanetsBackground implements Disposable {
    private final Texture[] planetTextures;

    public PlanetsBackground(MyAssetManager assetManager) {
        planetTextures = new Texture[]{
            assetManager.get("textures/Planet1.png", Texture.class),
            assetManager.get("textures/Planet2.png", Texture.class),
            assetManager.get("textures/Planet3.png", Texture.class),
            assetManager.get("textures/Planet4.png", Texture.class),
            assetManager.get("textures/Planet5.png", Texture.class),
            assetManager.get("textures/Planet6.png", Texture.class),
            assetManager.get("textures/Planet7.png", Texture.class),
            assetManager.get("textures/Planet8.png", Texture.class),
            assetManager.get("textures/Planet9.png", Texture.class),
            assetManager.get("textures/Planet10.png", Texture.class),
            assetManager.get("textures/Planet11.png", Texture.class),
            assetManager.get("textures/Planet12.png", Texture.class),
            assetManager.get("textures/Planet13.png", Texture.class),
            assetManager.get("textures/Planet14.png", Texture.class)
        };
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(planetTextures[0], -12, 135 - 93, 64, 64);
        batch.draw(planetTextures[1], 185, 135 - 38, 26, 26);
        batch.draw(planetTextures[2], 216, 135 - 94, 24, 24);
        batch.draw(planetTextures[3], 61, 135 - 37, 18, 18);
        batch.draw(planetTextures[4], 70, 135 - 109, 16, 16);
        batch.draw(planetTextures[5], 14, 135 - 110, 7, 7);
        batch.draw(planetTextures[6], 219, 135 - 131, 11, 11);
        batch.draw(planetTextures[7], 148, 135 - 97, 10, 10);
        batch.draw(planetTextures[8], 183, 135 - 74, 12, 12);
        batch.draw(planetTextures[9], 109, 135 - 87, 10, 10);
        batch.draw(planetTextures[10], 118, 135 - 128, 13, 13);
        batch.draw(planetTextures[11], 24, 135 - 18, 12, 12);
        batch.draw(planetTextures[12], 144, 135 - 13, 8, 8);
        batch.draw(planetTextures[13], 155, 135 - 132, 32, 32);
        batch.end();
    }

    @Override
    public void dispose() {
    }
}
