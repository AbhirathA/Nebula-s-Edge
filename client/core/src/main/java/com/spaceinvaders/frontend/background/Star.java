package com.spaceinvaders.frontend.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;

public class Star implements Disposable {
    // Position (x, y), size, and color of the star
    private final int x;
    private final int y;
    private final int size;
    private Color color;

    // List of colors the star will cycle through
    private static final List<Color> COLORS = List.of(
        new Color(1, 1, 1, 0.3f), // Faint white
        new Color(1, 1, 1, 0.6f), // Medium white
        new Color(1, 1, 1, 1.0f), // Bright white
        new Color(1, 1, 1, 0.6f), // Medium white (again)
        new Color(1, 1, 1, 0.3f)  // Faint white (again)
    );

    // Keeps track of which color the star is currently showing
    private int colorIndex;

    // Timer used to manage color transitions
    private float timer;

    // Boolean indicating whether the star is "dead" (finished color cycle)
    public boolean dead;

    /**
     * Constructor to initialize a star with a position, size, and color index.
     * @param x The x position of the star.
     * @param y The y position of the star.
     * @param size The size of the star.
     * @param colorIndex The initial color index for the star.
     */
    public Star(int x, int y, int size, int colorIndex) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.colorIndex = colorIndex;  // Set the initial color based on the colorIndex
        this.color = COLORS.get(colorIndex); // Get the color from the COLORS list
        this.timer = MathUtils.random(0f, 2f); // Randomize the initial timer value, so all the stars sparkle at random times

        this.dead = false; // Initially, the star is alive
    }

    /**
     * Renders the star on the screen with a color transition effect.
     * The color of the star changes over time based on the timer value.
     * @param shapeRenderer The ShapeRenderer used to draw the star.
     * @param deltaTime The time elapsed since the last frame.
     */
    public void render(ShapeRenderer shapeRenderer, float deltaTime) {
        // Set the current color of the star
        shapeRenderer.setColor(color);

        // Draw the star as a rectangle at the given position with the specified size
        shapeRenderer.rect(x, y, size, size);

        // Update the timer with the deltaTime to control color transitions
        timer += deltaTime;

        // If the timer exceeds 2 seconds, reset the timer and transition to the next color
        if (timer >= 2) {
            timer = 0;
            nextColor();
        }
    }

    /**
     * Changes the star's color by cycling through the colors in the COLORS list.
     * If the star has cycled through all colors, it is marked as dead.
     */
    public void nextColor() {
        // Increment the color index to move to the next color
        colorIndex = colorIndex + 1;

        // If the color index exceeds the list size, mark the star as dead
        if (colorIndex >= COLORS.size()) {
            dead = true;  // Star is dead after completing the color cycle
        } else {
            // Otherwise, set the next color from the list
            color = COLORS.get(colorIndex);
        }
    }

    /**
     * Dispose method from the Disposable interface.
     * This method would be used to release resources, but currently, no resources are managed.
     */
    @Override
    public void dispose() {
        // No resources to dispose of, as no external resources are allocated
    }
}
