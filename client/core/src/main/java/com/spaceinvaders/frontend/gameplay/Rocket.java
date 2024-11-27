package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class Rocket extends Actor {
    private final Sprite rocketSprite;
    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    public Rocket(MyAssetManager assetManager, float x, float y, float WORLD_WIDTH, float WORLD_HEIGHT) {
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Initialize rocket sprite
        Texture rocket = assetManager.get("textures/Rocket3.png", Texture.class);
        rocketSprite = new Sprite(rocket);
        rocketSprite.setPosition(x, y);
        rocketSprite.setOrigin(rocketSprite.getWidth() / 2, rocketSprite.getHeight() / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

//        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            rocketSprite.rotate(1);
//        }
//        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            rocketSprite.rotate(-1);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            moveInDirection(1);
//        }
//        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            moveInDirection(-1);
//        }

        rocketSprite.draw(batch);
    }

    public Sprite getRocketSprite() { return rocketSprite; }

    private void moveInDirection(float speed) {
        float rotation = rocketSprite.getRotation();
        float angleRad = (float) Math.toRadians(rotation) + 1.571f;

        float deltaX = MathUtils.cos(angleRad) * speed;
        float deltaY = MathUtils.sin(angleRad) * speed;

        rocketSprite.translate(deltaX, deltaY);

        rocketSprite.setX(MathUtils.clamp(rocketSprite.getX(), 0, WORLD_WIDTH - rocketSprite.getWidth()));
        rocketSprite.setY(MathUtils.clamp(rocketSprite.getY(), 0, WORLD_HEIGHT - rocketSprite.getHeight()));
    }
}
