package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.gameplay.GameplayStage;
import com.spaceinvaders.frontend.ui.UIStage;

public class GameplaySingleplayerScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private float CAMERA_WIDTH;
    private float CAMERA_HEIGHT;

    private float WORLD_WIDTH;
    private float WORLD_HEIGHT;

    private UIStage uiStage;

    private GameplayStage gameplayStage;

    InputMultiplexer multiplexer;

    public GameplaySingleplayerScreen(SpaceInvadersGame game, float CAMERA_WIDTH, float CAMERA_HEIGHT, float WORLD_WIDTH, float WORLD_HEIGHT) {
        this.game = game;
        this.CAMERA_WIDTH = CAMERA_WIDTH;
        this.CAMERA_HEIGHT = CAMERA_HEIGHT;
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
        viewport.apply();

        camera.setToOrtho(false);
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        camera.update();

        uiStage = new UIStage(game, new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT), ScreenState.SINGLEPLAYER_PAUSE);
        gameplayStage = new GameplayStage(game, viewport, WORLD_WIDTH, WORLD_HEIGHT, false);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); // UI input comes first
        multiplexer.addProcessor(gameplayStage); // Gameplay input
        multiplexer.addProcessor(new InputAdapter() { // Gameplay-specific input
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    pause();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        game.musicManager.play("gameplay");
        uiStage.setPaused(false);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.viewport.apply();
        this.camera.update();

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.camera.combined);

        updateCamera();

        Gdx.gl.glEnable(GL20.GL_BLEND); // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        gameplayStage.act(delta);
        gameplayStage.draw();

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        this.viewport.update(i, i1);
    }

    @Override
    public void pause() {
        game.musicManager.pause();
        game.screenManager.setScreen(ScreenState.SINGLEPLAYER_PAUSE);
        uiStage.setPaused(true);
    }

    @Override
    public void resume() {
        game.musicManager.resume();
        uiStage.setPaused(false);
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        uiStage.setPaused(true);
    }

    @Override
    public void dispose() {

    }

    private void updateCamera() {
        camera.position.set(gameplayStage.getRocketSprite().getX() + gameplayStage.getRocketSprite().getWidth() / 2, gameplayStage.getRocketSprite().getY() + gameplayStage.getRocketSprite().getHeight() / 2, 0);

        camera.position.x = MathUtils.clamp(camera.position.x, CAMERA_WIDTH / 2, WORLD_WIDTH - CAMERA_WIDTH / 2);
        camera.position.y = MathUtils.clamp(camera.position.y, CAMERA_HEIGHT / 2, WORLD_HEIGHT - CAMERA_HEIGHT / 2);

        camera.update();
    }
}
