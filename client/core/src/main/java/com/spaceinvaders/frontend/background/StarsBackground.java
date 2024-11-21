package com.spaceinvaders.frontend.background;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Iterator;

public class StarsBackground implements Disposable {
    // List to hold the stars in the background
    private final ArrayList<Star> starList;

    // Width and height of the game world
    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    /**
     * Constructor to initialize the stars in the background.
     * @param width The width of the game world (screen).
     * @param height The height of the game world (screen).
     * @param count The number of stars to generate in the background.
     */
    public StarsBackground(float width, float height, int count) {
        this.starList = new ArrayList<>();  // Initialize the star list

        this.WORLD_WIDTH = width;
        this.WORLD_HEIGHT = height;

        // Generate the specified number of stars with random color index
        for (int i = 0; i < count; i++) {
            starList.add(generateRandomStar((int) MathUtils.random(0, 4)));
        }
    }

    /**
     * Renders the stars in the background.
     * @param shapeRenderer The ShapeRenderer used to draw shapes (stars).
     * @param deltaTime The time elapsed since the last frame.
     */
    public void render(ShapeRenderer shapeRenderer, float deltaTime) {
        int starDeleted = 0;  // Counter for stars marked as dead (removed)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw the background rectangle (dark space color)
        shapeRenderer.setColor(29 / 255f, 27 / 255f, 41 / 255f, 1);
        shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Iterate through the list of stars and render them
        Iterator<Star> iterator = starList.iterator();
        while (iterator.hasNext()) {
            Star star = iterator.next();
            star.render(shapeRenderer, deltaTime);  // Render each star

            // If the star is marked as dead, remove it from the list
            if(star.dead) {
                iterator.remove();
                star.dispose();
                starDeleted++;  // Increment the counter for deleted stars to add that many stars again later
            }
        }

        shapeRenderer.end();

        // After rendering, regenerate stars that were removed
        for(int i = 0; i < starDeleted; i++) {
            starList.add(generateRandomStar(0));  // Add new random stars to the list
        }
    }

    /**
     * Generates a random star with a random position.
     * @param colorIndex The index for selecting the star's color from the color list.
     * @return A new Star object with random position and size.
     */
    private Star generateRandomStar(int colorIndex) {
        float x = MathUtils.random(0, WORLD_WIDTH);  // Random x position within the world width
        float y = MathUtils.random(0, WORLD_HEIGHT);  // Random y position within the world height
        int size = 1;  // Fixed size for the stars

        return new Star((int) x, (int) y, size, colorIndex);  // Create and return the new star
    }

    /**
     * Dispose method from the Disposable interface.
     * This method clears the star list when no longer needed to release resources.
     */
    @Override
    public void dispose() {
        for(Star star : starList) {
            star.dispose();
        }

        starList.clear();  // Clear the star list to release memory/resources
    }
}
