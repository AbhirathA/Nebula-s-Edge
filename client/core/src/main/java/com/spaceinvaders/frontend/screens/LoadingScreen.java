package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.spaceinvaders.frontend.SpaceInvadersGame;

public class LoadingScreen implements Screen {
    private final SpaceInvadersGame game;

    public LoadingScreen(SpaceInvadersGame game) {
        this.game = game;
        game.assetManager.loadAssets();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);

        if(game.assetManager.update()) {
            game.setScreen(new MainMenuScreen(game));
        }
        else {
            // Get the loading progress percentage
            float progress = game.assetManager.getProgress() * 100;

            // Start drawing
            game.batch.begin();
            game.font.setColor(1, 1, 1, 1);
            game.font.draw(game.batch, "Loading... " + (int) progress + "%", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f);
            game.batch.end();
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
