package com.spaceinvaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.SpaceInvadersGame;
import com.spaceinvaders.background.PlanetsBackground;
import com.spaceinvaders.background.StarsBackground;
import com.spaceinvaders.utils.ButtonUtils;

public class MainMenuScreen implements Screen {
    private final SpaceInvadersGame game;

    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private final Texture title;

    public MainMenuScreen(SpaceInvadersGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        initializeButtons();

        starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 50);
        planetsBackground = new PlanetsBackground(game.assetManager);

        title = game.assetManager.get("textures/title.png", Texture.class);
    }

    private void initializeButtons() {
        ImageTextButton singlePlayerButton = ButtonUtils.createScreenNavigationButton(
            game, "SinglePlayer", "textures/button.png", "textures/button.png", 93, 15, 73, 135 - 85, null);
        ImageTextButton multiPlayerButton = ButtonUtils.createScreenNavigationButton(
            game, "MultiPlayer", "textures/button.png", "textures/button.png", 93, 15, 73, 135 - 101, null);
        ImageTextButton optionsButton = ButtonUtils.createScreenNavigationButton(
            game, "Options", "textures/button.png", "textures/button.png", 93, 15, 73, 135 - 117, new LoginScreen(game));

        stage.addActor(singlePlayerButton);
        stage.addActor(multiPlayerButton);
        stage.addActor(optionsButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0,1);

        viewport.apply();
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);  // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        starsBackground.render(game.shapeRenderer, delta);  // Use delta for time
        planetsBackground.render(game.batch);

        game.batch.begin();
        game.batch.draw(title, 73, 135 - 66, 93, 47);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i, i1);
        stage.getViewport().update(i, i1, true);
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
        planetsBackground.dispose();
        title.dispose();
    }
}
