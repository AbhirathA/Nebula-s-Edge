package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class HealthBar {
    private Texture fullHeart;
    private Texture halfHeart;
    private Texture emptyHeart;
    private final float spacing = 1f;

    public HealthBar(MyAssetManager assetManager) {
        this.fullHeart = assetManager.get("textures/heart.png", Texture.class);
        this.halfHeart = assetManager.get("textures/halfHeart.png", Texture.class);
        this.emptyHeart = assetManager.get("textures/emptyHeart.png", Texture.class);
    }

    public void render(SpriteBatch batch, int currentHealth, int maxHealth, float x, float y) {
        float width = fullHeart.getWidth();

        currentHealth = Math.max(0, Math.min(currentHealth, maxHealth));
        int numFull = currentHealth / 2;
        int numHalf = currentHealth % 2;
        int numEmpty = (maxHealth - currentHealth) / 2;

        float startX = x;

        for(int i=0; i<numFull; i++) {
            batch.draw(fullHeart, startX, y);
            startX += width + spacing;
        }
        if(numHalf == 1) {
            batch.draw(halfHeart, startX, y);
            startX += width + spacing;
        }
        for(int i=0; i<numEmpty; i++) {
            batch.draw(emptyHeart, startX, y);
            startX += width + spacing;
        }
    }
}
