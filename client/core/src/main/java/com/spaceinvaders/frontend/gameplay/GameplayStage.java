package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;

public class GameplayStage extends Stage {
    private final SpaceInvadersGame game;
    private final StarsBackground starsBackground;
    private final Rocket rocket;

    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    public GameplayStage(SpaceInvadersGame game, Viewport viewport, float WORLD_WIDTH, float WORLD_HEIGHT) {
        super(viewport);
        this.game = game;
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Initialize stars background
        starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 200);

        // Initialize rocket
        rocket = new Rocket(game.assetManager, WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f, WORLD_WIDTH, WORLD_HEIGHT);
        addActor(rocket);
    }

    @Override
    public void draw() {
        // Render rocket sprite
        game.batch.setProjectionMatrix(getViewport().getCamera().combined);
        super.draw();
    }

    @Override
    public void act(float delta) {
        // Render background
        starsBackground.render(game.shapeRenderer, delta);

        super.act(delta);
    }

    public Sprite getRocketSprite() { return rocket.getRocketSprite(); }
}
