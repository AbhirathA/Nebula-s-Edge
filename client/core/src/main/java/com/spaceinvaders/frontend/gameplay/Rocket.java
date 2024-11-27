package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class Rocket extends Actor {

    // The Sprite representing the rocket
    private final Sprite rocketSprite;

    // Width and height of the game world, used to constrain the rocket's movement
    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    // Constructor for the Rocket class
    public Rocket(MyAssetManager assetManager, float x, float y, float WORLD_WIDTH, float WORLD_HEIGHT) {
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Load the rocket texture from the asset manager
        Texture rocket = assetManager.get("textures/Rocket3.png", Texture.class);

        // Initialize the rocket sprite with the loaded texture
        rocketSprite = new Sprite(rocket);

        // Set the initial position of the rocket
        rocketSprite.setPosition(x, y);

        // Set the origin of the rocket sprite (for rotation)
        rocketSprite.setOrigin(rocketSprite.getWidth() / 2, rocketSprite.getHeight() / 2);
    }

    // This method is called each frame for the Rocket actor
    @Override
    public void act(float delta) {
        super.act(delta);

        // Additional logic for handling movement or other actions can be added here in the future
    }

    // This method is responsible for drawing the rocket sprite to the screen
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Draw the rocket sprite on the screen at the rocket's current position and with the current rotation
        rocketSprite.draw(batch);
    }

    // Getter method to access the rocket sprite
    public Sprite getRocketSprite() {
        return rocketSprite;
    }

    // Move the rocket in the direction it's facing, based on its speed
    private void moveInDirection(float speed) {
        // Get the current rotation of the rocket
        float rotation = rocketSprite.getRotation();

        // Convert the rotation angle from degrees to radians (and adjust for the default angle)
        float angleRad = (float) Math.toRadians(rotation) + 1.571f;  // 1.571f is a constant to adjust for angle correction

        // Calculate the change in x and y position based on the speed and rotation
        float deltaX = MathUtils.cos(angleRad) * speed;
        float deltaY = MathUtils.sin(angleRad) * speed;

        // Move the rocket by the calculated deltaX and deltaY
        rocketSprite.translate(deltaX, deltaY);

        // Keep the rocket within the bounds of the screen (clamp its position)
        rocketSprite.setX(MathUtils.clamp(rocketSprite.getX(), 0, WORLD_WIDTH - rocketSprite.getWidth()));
        rocketSprite.setY(MathUtils.clamp(rocketSprite.getY(), 0, WORLD_HEIGHT - rocketSprite.getHeight()));
    }
}
