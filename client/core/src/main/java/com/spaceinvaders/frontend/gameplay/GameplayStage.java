package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;

public class GameplayStage extends Stage {
    private final SpaceInvadersGame game;
    private final StarsBackground starsBackground;
    private final Sprite rocketSprite;

    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    public GameplayStage(SpaceInvadersGame game, Viewport viewport, float WORLD_WIDTH, float WORLD_HEIGHT) {
        super(viewport);
        this.game = game;
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Initialize stars background
        starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 200);

        // Initialize rocket sprite
        Texture rocket = game.assetManager.get("textures/Rocket3.png", Texture.class);
        rocketSprite = new Sprite(rocket);
        rocketSprite.setPosition(WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f);
        rocketSprite.setSize(21, 21);
        rocketSprite.setOrigin(rocketSprite.getWidth() / 2, rocketSprite.getHeight() / 2);
    }

    @Override
    public void draw() {
        // Render rocket sprite
        game.batch.setProjectionMatrix(getViewport().getCamera().combined);
        game.batch.begin();
        rocketSprite.draw(game.batch);
        game.batch.end();

        super.draw();
    }

    @Override
    public void act(float delta) {
        // Render background
        starsBackground.render(game.shapeRenderer, delta);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rocketSprite.rotate(1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rocketSprite.rotate(-1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveInDirection(1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveInDirection(-1);
        }

        super.act(delta);
    }

    private void moveInDirection(float speed) {
        float rotation = rocketSprite.getRotation();
        float angleRad = (float) Math.toRadians(rotation) + 1.571f;

        float deltaX = MathUtils.cos(angleRad) * speed;
        float deltaY = MathUtils.sin(angleRad) * speed;

        rocketSprite.translate(deltaX, deltaY);

        rocketSprite.setX(MathUtils.clamp(rocketSprite.getX(), 0, WORLD_WIDTH - rocketSprite.getWidth()));
        rocketSprite.setY(MathUtils.clamp(rocketSprite.getY(), 0, WORLD_HEIGHT - rocketSprite.getHeight()));
    }

    public Sprite getRocketSprite() { return rocketSprite; }
}
