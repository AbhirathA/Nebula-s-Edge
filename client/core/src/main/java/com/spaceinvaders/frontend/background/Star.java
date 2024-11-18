package com.spaceinvaders.frontend.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;

public class Star implements Disposable {
    private final int x;
    private final int y;
    private final int size;
    private Color color;
    private static final List<Color> COLORS = List.of(new Color(1, 1, 1, 0.3f), new Color(1, 1, 1, 0.6f), new Color(1, 1, 1, 1.0f), new Color(1, 1, 1, 0.6f), new Color(1, 1, 1, 0.3f));
    private int colorIndex;
    private float timer;

    public boolean dead;

    public Star(int x, int y, int size, int colorIndex) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.colorIndex = colorIndex;  // Start with the first color
        this.color = COLORS.get(colorIndex);
        this.timer = MathUtils.random(0f, 2f);

        this.dead = false;
    }

    public void render(ShapeRenderer shapeRenderer, float deltaTime) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, size, size);

        timer += deltaTime;

        // Interpolate the color based on the timer
        if(timer >= 2){
            timer = 0;
            nextColor();
        }
    }

    public void nextColor() {
        // Cycle through the colors
        colorIndex = colorIndex + 1;
        if (colorIndex >= COLORS.size()) {
            dead = true;  // Mark the star as dead if it completes the cycle
        } else {
            color = COLORS.get(colorIndex);  // Set the next color
        }
    }

    @Override
    public void dispose() {

    }
}
