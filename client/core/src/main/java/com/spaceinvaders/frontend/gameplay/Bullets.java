package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;

/**
 * The Bullets class manages the rendering of bullet sprites in the game.
 * It holds the bullet texture and renders it at specific positions and rotations
 * based on the provided coordinates for each bullet in the game world.
 */
public class Bullets implements Entity {
    // A Sprite object representing the bullet texture
    private final Sprite bullet;

    /**
     * Constructor to initialize the bullet sprite by loading the texture using the asset manager.
     *
     * @param assetManager The asset manager used to load the bullet texture.
     */
    public Bullets(MyAssetManager assetManager) {
        // Load the bullet texture and create a sprite for it
        bullet = new Sprite(assetManager.get("textures/bullet.png", Texture.class));
    }

    /**
     * Renders the bullets at their respective positions and rotations based on the coordinates
     * provided in the coordinate list. Each bullet is drawn using the bullet sprite.
     *
     * @param batch            The batch used for drawing the sprite.
     * @param coordinateList   A list of coordinates that contains information about the position,
     *                         rotation, and other properties of each bullet in the game world.
     */
    @Override
    public void render(Batch batch, ArrayList<Coordinate> coordinateList) {
        // Iterate through each coordinate in the list
        for (Coordinate coordinate : coordinateList) {
            bullet.setPosition(coordinate.x, coordinate.y);
            bullet.setRotation(coordinate.angle);

            // Draw the bullet sprite to the batch, rendering it to the screen
            bullet.draw(batch);
        }
    }
}
