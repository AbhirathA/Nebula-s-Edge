package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;

/**
 * The Powerups class handles the rendering of powerup sprites (such as attack, health, and bonus powerups).
 * It loads the textures for each powerup and draws the corresponding sprite at the correct position and rotation.
 */
public class Powerups implements Entity {
    // Sprites for different types of powerups (attack, health, bonus)
    private final Sprite attack;
    private final Sprite health;
    private final Sprite bonus;

    /**
     * Constructor to initialize the powerup sprites by loading the corresponding textures from the asset manager.
     *
     * @param assetManager The asset manager used to load the powerup textures.
     */
    public Powerups(MyAssetManager assetManager) {
        // Load textures for the three powerups and create sprites for them
        attack = new Sprite(assetManager.get("textures/powerup1.png", Texture.class));
        health = new Sprite(assetManager.get("textures/powerup2.png", Texture.class));
        bonus = new Sprite(assetManager.get("textures/powerup3.png", Texture.class));
    }

    /**
     * Renders the powerup sprites at the specified coordinates with their respective rotations.
     * Based on the coordinate type, it selects the appropriate sprite to draw.
     *
     * @param batch            The batch used to draw the sprites to the screen.
     * @param coordinateList   A list of coordinates representing the position, rotation, and type of each powerup.
     */
    @Override
    public void render(Batch batch, ArrayList<Coordinate> coordinateList) {
        // Iterate through the list of coordinates to render each powerup
        for(Coordinate coordinate : coordinateList) {
            // Determine the correct sprite based on the powerup type in the coordinate
            Sprite sprite;
            switch (coordinate.type) {
                case "attack":
                    sprite = attack;  // Attack powerup sprite
                    break;
                case "health":
                    sprite = health;  // Health powerup sprite
                    break;
                case "bonus":
                    sprite = bonus;   // Bonus powerup sprite
                    break;
                default:
                    // Throw an exception if the powerup type is invalid
                    throw new IllegalArgumentException("Invalid powerup type: " + coordinate.type);
            }

            // Set the position and rotation of the powerup sprite based on the coordinate data
            sprite.setPosition(coordinate.getX() - sprite.getWidth() / 2f, coordinate.getY() - sprite.getHeight() / 2f);
            sprite.setRotation(coordinate.getAngle());

            // Draw the powerup sprite to the batch, rendering it to the screen
            sprite.draw(batch);
        }
    }
}
