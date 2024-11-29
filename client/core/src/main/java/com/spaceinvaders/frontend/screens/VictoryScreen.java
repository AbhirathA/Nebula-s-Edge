package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.ui.GameOver;
import com.spaceinvaders.frontend.ui.Victory;

public class VictoryScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    public VictoryScreen(SpaceInvadersGame game, StarsBackground starsBackground, PlanetsBackground planetsBackground) {
        this.game = game;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(SpaceInvadersGame.WORLD_WIDTH, SpaceInvadersGame.WORLD_HEIGHT, this.camera);
        Viewport stageViewport = new FitViewport(SpaceInvadersGame.STAGE_WIDTH, SpaceInvadersGame.STAGE_HEIGHT);

        this.camera.position.set(SpaceInvadersGame.WORLD_WIDTH / 2, SpaceInvadersGame.WORLD_HEIGHT / 2, 0);
        this.camera.update();

        this.stage = new Stage(stageViewport);

        this.initialiseActors();

        this.starsBackground = starsBackground;
        this.planetsBackground = planetsBackground;
    }

    @Override
    public void show() {
        game.musicManager.pause();
        Gdx.input.setInputProcessor(this.stage);
        game.soundManager.play("victory");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.viewport.apply();
        this.camera.update();

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND); // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.starsBackground.render(this.game.shapeRenderer, delta); // Use delta for time
        this.planetsBackground.render(this.game.batch);

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        game.musicManager.pause();
    }

    @Override
    public void resume() {
        game.musicManager.pause();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    private void initialiseActors() {
        Victory victory = new Victory(this.game);

        stage.addActor(victory);
    }
}
