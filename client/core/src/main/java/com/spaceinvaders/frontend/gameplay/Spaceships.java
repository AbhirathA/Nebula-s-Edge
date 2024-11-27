package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Spaceships class handles the rendering of spaceship sprites.
 * It loads the textures for the spaceships and draws them at the correct position and rotation
 * based on the data provided in the coordinate list.
 */
public class Spaceships {
    // Array to store the sprites for the three different spaceship textures
    private final Sprite[] spaceships;

    // A map to associate each spaceship ID with a sprite index (used for cycling through different spaceship textures)
    private final HashMap<Integer, Integer> entitySpriteMap;

    /**
     * Constructor to initialize the spaceship sprites by loading the corresponding textures.
     *
     * @param assetManager The asset manager used to load the spaceship textures.
     */
    public Spaceships(MyAssetManager assetManager) {
        // Initialize the array to store the spaceship sprites (3 spaceships in total)
        spaceships = new Sprite[3];

        // Initialize the entity sprite map that will map spaceship IDs to specific sprites
        entitySpriteMap = new HashMap<>();

        // Load the textures for the spaceships (Rocket1.png, Rocket2.png, Rocket3.png) and create sprite objects for each
        for(int i=1; i<=3; i++) {
            spaceships[i-1] = new Sprite(assetManager.get("textures/Rocket" + i + ".png", Texture.class));
        }
    }

    /**
     * Renders the spaceship sprites at the specified coordinates with their respective rotations.
     * This method ensures that the correct sprite is drawn for each spaceship based on its ID.
     *
     * @param batch            The batch used to draw the sprites to the screen.
     * @param coordinateList   A list of coordinates representing the position, rotation, and ID of each spaceship.
     * @param id               The ID of the spaceship that needs to be excluded from rendering.
     */
    public void renderSpaceships(Batch batch, ArrayList<Coordinate> coordinateList, int id) {
        // Iterate through the list of coordinates to render each spaceship
        for(Coordinate coordinate : coordinateList) {
            // Skip rendering if the spaceship's ID matches the provided ID (typically the player's spaceship)
            if(coordinate.id == id) {
                break;  // Exit the loop if the spaceship's ID matches the excluded ID
            }

            // Check if the spaceship's ID is already in the map, if not, associate it with a sprite index
            if(!entitySpriteMap.containsKey(coordinate.id)) {
                // Use the modulo operator to cycle through the 3 available spaceship sprites
                entitySpriteMap.put(coordinate.id, coordinate.id % 3);
            }

            // Get the sprite corresponding to the spaceship's ID
            Sprite sprite = spaceships[entitySpriteMap.get(coordinate.id)];

            // Print the spaceship's ID (for debugging purposes)
            System.out.println(coordinate.id);

            // Set the position of the spaceship sprite based on the coordinate's x and y values
            sprite.setPosition(coordinate.x, coordinate.y);

            // Set the rotation of the spaceship sprite based on the coordinate's angle
            sprite.setRotation(coordinate.angle);

            // Draw the spaceship sprite to the batch, rendering it to the screen
            sprite.draw(batch);
        }
    }
}
