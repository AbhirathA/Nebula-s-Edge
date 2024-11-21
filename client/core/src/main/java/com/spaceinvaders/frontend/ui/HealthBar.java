package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class HealthBar extends Actor {
    private float x, y;
    private Texture fullHeart;
    private Texture halfHeart;
    private Texture emptyHeart;
    private final float spacing = 1f;
    private int currentHealth;
    private int maxHealth;

    public HealthBar(MyAssetManager assetManager, float x, float y, int maxHealth) {
        this.x = x;
        this.y = y;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;  // Start with full health

        this.fullHeart = assetManager.get("textures/heart.png", Texture.class);
        this.halfHeart = assetManager.get("textures/halfHeart.png", Texture.class);
        this.emptyHeart = assetManager.get("textures/emptyHeart.png", Texture.class);

        setBounds(x, y, fullHeart.getWidth(), fullHeart.getHeight());  // Set the size of the HealthBar based on the heart texture width
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Add any logic for health updates (e.g., decrement health)
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float width = fullHeart.getWidth();
        float startX = getX();

        // Make sure currentHealth is within bounds
        currentHealth = Math.max(0, Math.min(currentHealth, maxHealth));

        int numFull = currentHealth / 2;
        int numHalf = currentHealth % 2;
        int numEmpty = (maxHealth - currentHealth) / 2;

        // Render full hearts
        for (int i = 0; i < numFull; i++) {
            batch.draw(fullHeart, startX, getY());
            startX += width + spacing;
        }

        // Render half heart if needed
        if (numHalf == 1) {
            batch.draw(halfHeart, startX, getY());
            startX += width + spacing;
        }

        // Render empty hearts
        for (int i = 0; i < numEmpty; i++) {
            batch.draw(emptyHeart, startX, getY());
            startX += width + spacing;
        }
    }

    // Methods to modify health
    public void setHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth));
    }

    public void changeHealth(int amount) {
        setHealth(currentHealth + amount);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
