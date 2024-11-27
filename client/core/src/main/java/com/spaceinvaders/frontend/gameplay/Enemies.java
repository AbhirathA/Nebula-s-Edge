package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Enemies class handles the rendering of enemy sprites in the game.
 * It maintains an array of enemy sprites and uses a map to track which sprite corresponds
 * to which enemy entity based on their ID. The class provides functionality to render
 * enemies at their respective positions and rotations.
 */
public class Enemies {
    // Array to hold the different enemy sprites (up to 12)
    private final Sprite[] enemies;

    // HashMap to map each enemy's ID to a specific sprite index
    private final HashMap<Integer, Integer> entitySpriteMap;

    /**
     * Constructor that initializes the enemies' sprite array by loading textures from the asset manager.
     *
     * @param assetManager The asset manager used to load textures for the enemy sprites.
     */
    public Enemies(MyAssetManager assetManager) {
        // Initialize the sprite array with 12 possible enemies
        enemies = new Sprite[12];

        // Initialize the entitySpriteMap to store the mapping of entity IDs to sprite indices
        entitySpriteMap = new HashMap<>();

        // Load textures for the 12 different enemy sprites
        for (int i = 1; i <= 12; i++) {
            // Load each texture and create a sprite for it
            enemies[i - 1] = new Sprite(assetManager.get("textures/enemy" + i + ".png", Texture.class));
        }
    }

    /**
     * Renders all enemies based on their coordinates and corresponding sprite.
     * The enemies are drawn at their respective positions and rotations.
     *
     * @param batch            The batch used for drawing the sprites.
     * @param coordinateList   A list of coordinates that specifies the position and rotation
     *                         of each enemy in the game world.
     */
    public void renderEnemies(Batch batch, ArrayList<Coordinate> coordinateList) {
        // Iterate through the list of coordinates for the enemies
        for (Coordinate coordinate : coordinateList) {
            // If the entity's ID hasn't been mapped to a sprite yet, do so
            if (!entitySpriteMap.containsKey(coordinate.id)) {
                // Use the entity's ID modulo 12 to determine the sprite index
                entitySpriteMap.put(coordinate.id, coordinate.id % 12);
            }

            // Get the sprite for this enemy using the mapped index
            Sprite sprite = enemies[entitySpriteMap.get(coordinate.id)];

            // Set the position and rotation of the sprite based on the coordinate
            sprite.setPosition(coordinate.x, coordinate.y);
            sprite.setRotation(coordinate.angle);

            // Draw the sprite to the batch (render it to the screen)
            sprite.draw(batch);
        }
    }
}
