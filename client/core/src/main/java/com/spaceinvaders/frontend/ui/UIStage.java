package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;

public class UIStage extends Stage {
    private HealthBar healthBar;

    public UIStage(SpaceInvadersGame game, Viewport viewport) {
        super(viewport, game.batch);

        healthBar = new HealthBar(game.assetManager, 2, viewport.getWorldHeight() - 11, 20);
        addActor(healthBar);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }
}
