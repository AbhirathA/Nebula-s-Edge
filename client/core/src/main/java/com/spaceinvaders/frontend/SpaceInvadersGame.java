package com.spaceinvaders.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spaceinvaders.frontend.managers.ScreenManager;
import com.spaceinvaders.frontend.managers.MyAssetManager;
import com.spaceinvaders.frontend.screens.ScreenState;

public class SpaceInvadersGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;

    public final MyAssetManager assetManager = new MyAssetManager();
    public final ScreenManager screenManager = ScreenManager.getInstance(this);

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        screenManager.setScreen(ScreenState.LOADING);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        ScreenManager.getInstance(this).dispose();
    }
}
