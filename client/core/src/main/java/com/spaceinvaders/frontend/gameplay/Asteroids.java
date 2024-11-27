package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Asteroids class manages the creation and rendering of asteroid sprites
 * in the game. It handles different types of asteroids (big, medium, small)
 * by storing their respective textures and rendering them based on the
 * coordinates provided for each asteroid in the game world.
 */
public class Asteroids {
    // Arrays to hold sprites for different types of asteroids (big, medium, small)
    private final Sprite[] bigAsteroids;
    private final Sprite[] mediumAsteroids;
    private final Sprite[] smallAsteroids;

    // HashMap to store the relationship between asteroid IDs and the index of the sprite to be used
    private final HashMap<Integer, Integer> entitySpriteMap;

    /**
     * Constructor to initialize the asteroid arrays and load the textures using the asset manager.
     * It loads 7 different sprites for each type of asteroid (big, medium, small).
     *
     * @param assetManager The asset manager used to load textures for the asteroids.
     */
    public Asteroids(MyAssetManager assetManager) {
        // Initialize the arrays for 7 sprites for each asteroid type
        bigAsteroids = new Sprite[7];
        mediumAsteroids = new Sprite[7];
        smallAsteroids = new Sprite[7];

        // Initialize the HashMap for mapping asteroid IDs to sprite indices
        entitySpriteMap = new HashMap<>();

        // Load textures for big asteroids (bigAst1.png to bigAst7.png)
        for (int i = 1; i <= 7; i++) {
            bigAsteroids[i - 1] = new Sprite(assetManager.get("textures/bigAst" + i + ".png", Texture.class));
        }

        // Load textures for medium asteroids (midAst1.png to midAst7.png)
        for (int i = 1; i <= 7; i++) {
            mediumAsteroids[i - 1] = new Sprite(assetManager.get("textures/midAst" + i + ".png", Texture.class));
        }

        // Load textures for small asteroids (smallAst1.png to smallAst7.png)
        for (int i = 1; i <= 7; i++) {
            smallAsteroids[i - 1] = new Sprite(assetManager.get("textures/smallAst" + i + ".png", Texture.class));
        }
    }

    /**
     * Renders the asteroids at their respective positions and rotations based on the coordinates
     * provided in the coordinate list. The correct sprite is chosen based on the type of the asteroid
     * (big, medium, small) and the ID of the asteroid.
     *
     * @param batch            The batch used for drawing the sprites.
     * @param coordinateList   A list of coordinates that contains information about the position,
     *                         rotation, and type of each asteroid in the game world.
     */
    public void renderAsteroids(Batch batch, ArrayList<Coordinate> coordinateList) {
        // Iterate through each coordinate in the provided list
        for (Coordinate coordinate : coordinateList) {
            // If the asteroid ID is not already in the map, assign it a sprite index based on ID % 7
            if (!entitySpriteMap.containsKey(coordinate.id)) {
                entitySpriteMap.put(coordinate.id, coordinate.id % 7);
            }

            Sprite sprite;

            // Determine the correct sprite based on the asteroid type
            switch (coordinate.type) {
                case "B":  // Big asteroid
                    sprite = bigAsteroids[entitySpriteMap.get(coordinate.id)];
                    break;
                case "M":  // Medium asteroid
                    sprite = mediumAsteroids[entitySpriteMap.get(coordinate.id)];
                    break;
                case "S":  // Small asteroid
                    sprite = smallAsteroids[entitySpriteMap.get(coordinate.id)];
                    break;
                default:
                    // If the asteroid type is invalid, throw an exception
                    throw new IllegalArgumentException("Invalid asteroid type: " + coordinate.type);
            }

            // Set the position and rotation of the sprite based on the coordinate data
            sprite.setPosition(coordinate.x, coordinate.y);
            sprite.setRotation(coordinate.angle);

            // Draw the sprite to the batch (which will render it to the screen)
            sprite.draw(batch);
        }
    }
}
