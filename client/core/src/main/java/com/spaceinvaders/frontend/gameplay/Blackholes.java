package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;

/**
 * The Blackholes class is responsible for handling the rendering of blackhole sprites in the game.
 * It loads the blackhole texture and draws it at the specified coordinates and rotations.
 */
public class Blackholes {
    // Sprite object to hold the blackhole texture
    private final Sprite blackhole;

    /**
     * Constructor to initialize the blackhole sprite by loading the texture from the asset manager.
     *
     * @param assetManager The asset manager used to load the blackhole texture.
     */
    public Blackholes(MyAssetManager assetManager) {
        // Load the blackhole texture and create a sprite for it
        blackhole = new Sprite(assetManager.get("textures/blackhole.png", Texture.class));
    }

    /**
     * Renders the blackhole sprites at the specified coordinates with their respective rotations.
     *
     * @param batch            The batch used to draw the sprite to the screen.
     * @param coordinateList   A list of coordinates representing the position and rotation of each blackhole.
     */
    public void renderBullet(Batch batch, ArrayList<Coordinate> coordinateList) {
        // Iterate through the list of coordinates to render each blackhole
        for (Coordinate coordinate : coordinateList) {
            blackhole.setPosition(coordinate.x, coordinate.y);
            blackhole.setRotation(coordinate.angle);

            // Draw the blackhole sprite to the batch, rendering it to the screen
            blackhole.draw(batch);
        }
    }
}
