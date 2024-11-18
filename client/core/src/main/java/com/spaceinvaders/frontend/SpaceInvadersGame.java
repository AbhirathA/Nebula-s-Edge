package com.spaceinvaders.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spaceinvaders.frontend.managers.ScreenManager;
import com.spaceinvaders.frontend.managers.MyAssetManager;
import com.spaceinvaders.frontend.screens.LoadingScreen;
import com.spaceinvaders.frontend.screens.ScreenState;
import com.spaceinvaders.frontend.utils.Command;

public class SpaceInvadersGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;

    public MyAssetManager assetManager;
    public ScreenManager screenManager;

    private class CommandClass implements Command {

        @Override
        public void execute() {
            SpaceInvadersGame.this.assetManager.loadAssets();
        }

        @Override
        public void onUpdate() {
            SpaceInvadersGame.this.screenManager = new ScreenManager(SpaceInvadersGame.this);
            SpaceInvadersGame.this.screenManager.setScreen(ScreenState.LOGIN_GATEWAY);
        }

        @Override
        public boolean update() {
            return SpaceInvadersGame.this.assetManager.update();
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        assetManager = new MyAssetManager();

        setScreen(new LoadingScreen(this, new CommandClass()));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        ScreenManager.getInstance(this).dispose();
    }
}
