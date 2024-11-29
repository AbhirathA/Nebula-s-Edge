package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.screens.ScreenState;
import com.spaceinvaders.frontend.utils.ButtonUtils;

public class GameOver extends Table {

    public GameOver(SpaceInvadersGame game) {
        setFillParent(true);
        center();

        Image gameOver = new Image(game.assetManager.get("textures/gameOver.png", Texture.class));
        add(gameOver).colspan(2).padBottom(5).size(163.35f, 90.45f);
        row();

        ImageButton menuButton = ButtonUtils.createScreenNavigationButton(game, "textures/menu.png", "textures/menu.png", 38, 38,
            0, 0, ScreenState.MAIN_MENU);
        ImageButton restartButton = ButtonUtils.createImageButton(game, "textures/restart.png", "textures/restart.png",
            38, 38, 0, 0);

        add(menuButton).size(20, 20).right().pad(2);
        add(restartButton).size(20, 20).left().pad(2);
    }

}
