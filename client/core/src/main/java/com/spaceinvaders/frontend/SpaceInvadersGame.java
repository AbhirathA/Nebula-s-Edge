package com.spaceinvaders.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spaceinvaders.frontend.managers.ScreenManager;
import com.spaceinvaders.frontend.managers.MyAssetManager;
import com.spaceinvaders.frontend.screens.LoadingScreen;
import com.spaceinvaders.frontend.screens.ScreenState;

public class SpaceInvadersGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;

    public MyAssetManager assetManager;
    public ScreenManager screenManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        assetManager = new MyAssetManager();

        setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        ScreenManager.getInstance(this).dispose();
    }
}
