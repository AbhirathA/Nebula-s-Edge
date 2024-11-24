package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceinvaders.frontend.managers.MyAssetManager;

/**
 * HealthBar is a UI component that visually represents a player's health
 * using a series of heart icons (full, half, and empty).
 */
public class HealthBar extends Actor {
    private float x, y;
    private Texture fullHeart;
    private Texture halfHeart;
    private Texture emptyHeart;
    private final float spacing = 1f;
    private int currentHealth;
    private int maxHealth;

    /**
     * Constructor to initialize the HealthBar.
     *
     * @param assetManager The asset manager to load textures.
     * @param x            The x-coordinate of the health bar.
     * @param y            The y-coordinate of the health bar.
     * @param maxHealth    The maximum health value.
     */
    public HealthBar(MyAssetManager assetManager, float x, float y, int maxHealth) {
        this.x = x;
        this.y = y;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; // Start with full health

        // Load heart textures from the asset manager
        this.fullHeart = assetManager.get("textures/heart.png", Texture.class);
        this.halfHeart = assetManager.get("textures/halfHeart.png", Texture.class);
        this.emptyHeart = assetManager.get("textures/emptyHeart.png", Texture.class);

        // Set the bounds of the HealthBar based on the heart texture dimensions
        setBounds(x, y, fullHeart.getWidth(), fullHeart.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    // Called to render the health bar
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float width = fullHeart.getWidth();
        float startX = getX();

        // Ensure currentHealth is within valid bounds
        currentHealth = Math.max(0, Math.min(currentHealth, maxHealth));

        // Calculate the number of full, half, and empty hearts to draw
        int numFull = currentHealth / 2;
        int numHalf = currentHealth % 2;
        int numEmpty = (maxHealth - currentHealth) / 2;

        // Draw full hearts
        for (int i = 0; i < numFull; i++) {
            batch.draw(fullHeart, startX, getY(), 9, 9);
            startX += width + spacing;
        }

        // Draw half heart if present
        if (numHalf == 1) {
            batch.draw(halfHeart, startX, getY(), 9, 9);
            startX += width + spacing;
        }

        // Draw empty hearts
        for (int i = 0; i < numEmpty; i++) {
            batch.draw(emptyHeart, startX, getY(), 9, 9);
            startX += width + spacing;
        }
    }

    // Setter method for currentHealth
    public void setHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth)); // Ensure health is within bounds
    }

    /**
     * Adjusts the current health by a specified amount.
     *
     * @param amount The amount to change health by (positive or negative).
     */
    public void changeHealth(int amount) {
        setHealth(currentHealth + amount); // Modify health and ensure bounds
    }

    // Getter method for currentHealth
    public int getCurrentHealth() {
        return currentHealth;
    }

    // Getter method for maxHealth
    public int getMaxHealth() {
        return maxHealth;
    }
}
