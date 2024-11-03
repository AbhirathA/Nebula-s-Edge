package com.spaceinvaders.frontend.background;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Iterator;

public class StarsBackground implements Disposable {
    private final ArrayList<Star> starList;

    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    public StarsBackground(float width, float height, int count) {
        starList = new ArrayList<>();

        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;

        for (int i = 0; i < count; i++) {
            starList.add(generateRandomStar((int) MathUtils.random(0, 4)));
        }
    }

    public void render(ShapeRenderer shapeRenderer, float deltaTime) {
        int starDeleted = 0;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(29 / 255f, 27 / 255f, 41 / 255f, 1);
        shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        Iterator<Star> iterator = starList.iterator();
        while (iterator.hasNext()) {
            Star star = iterator.next();
            star.render(shapeRenderer, deltaTime);

            if(star.dead) {
                iterator.remove();
                starDeleted++;
            }
        }

        shapeRenderer.end();

        for(int i=0; i<starDeleted; i++){
            starList.add(generateRandomStar(0));
        }
    }

    // Generate a star with random position and size
    private Star generateRandomStar(int colorIndex) {
        float x = MathUtils.random(0, WORLD_WIDTH);
        float y = MathUtils.random(0, WORLD_HEIGHT);
        int size = 1;  // Random star size (1-3 pixels)

        return new Star((int) x, (int) y, size, colorIndex);
    }

    @Override
    public void dispose() {
        for (Star star : starList) {
            star.dispose();
        }
        starList.clear();
    }
}
