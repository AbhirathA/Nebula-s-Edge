package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.screens.ScreenState;
import com.spaceinvaders.frontend.utils.ButtonUtils;

public class UIStage extends Stage {
    private HealthBar healthBar;

    public UIStage(SpaceInvadersGame game, Viewport viewport) {
        super(viewport, game.batch);

        healthBar = new HealthBar(game.assetManager, 2, viewport.getWorldHeight() - 11, 10);
        ImageButton pauseButton = ButtonUtils.createScreenNavigationButton(game, "textures/pause.png", "textures/pause.png", 7, 7, viewport.getWorldWidth() - 10, viewport.getWorldHeight() - 10, ScreenState.PAUSE);

        addActor(healthBar);
        addActor(pauseButton);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }
}
