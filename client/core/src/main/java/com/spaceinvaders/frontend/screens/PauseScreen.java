package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.SliderUtils;

public class PauseScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final float STAGE_WIDTH;

    private final Stage stage;

    private final StarsBackground starsBackground;

    public PauseScreen(SpaceInvadersGame game, float WORLD_WIDTH, float WORLD_HEIGHT, float STAGE_WIDTH,
            float STAGE_HEIGHT, StarsBackground starsBackground) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        Viewport stageViewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT);

        this.STAGE_WIDTH = STAGE_WIDTH;

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(stageViewport);
        initializeActors();

        this.starsBackground = starsBackground;
    }

    @Override
    public void show() {
        game.musicManager.play("gameplay");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND); // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        starsBackground.render(game.shapeRenderer, delta); // Use delta for time

        game.batch.begin();
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
        game.musicManager.pause();
    }

    @Override
    public void resume() {
        game.musicManager.resume();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    private void initializeActors() {

        ImageButton menuButton = ButtonUtils.createScreenNavigationButton(game, "textures/menu.png", "textures/menu.png", 38, 38,
                (STAGE_WIDTH - 38) / 2f - 58, 80, ScreenState.MAIN_MENU);
        ImageButton playButton = ButtonUtils.createScreenNavigationButton(game, "textures/play.png", "textures/play.png", 38, 38,
                (STAGE_WIDTH - 38) / 2f, 80, ScreenState.SINGLEPLAYER_GAMEPLAY);
        ImageButton restartButton = ButtonUtils.createImageButton(game, "textures/restart.png", "textures/restart.png",
                38, 38, (STAGE_WIDTH - 38) / 2f + 58, 80);

        Image musicIcon = new Image(game.assetManager.get("textures/music.png", Texture.class));
        musicIcon.setSize(20, 20);
        musicIcon.setPosition((STAGE_WIDTH - 150) / 2f - 29, 155);
        Slider musicSlider = SliderUtils.createSlider(game.assetManager, 150, 20, (STAGE_WIDTH - 150) / 2f, 155);
        Label musicLabel = SliderUtils.getValueLabel(game.assetManager, musicSlider, 150, 20, (STAGE_WIDTH - 150) / 2f,
                155);

        musicSlider.setValue(game.musicManager.getVolume() * 200);
        musicSlider.addListener(event -> {
            game.musicManager.setVolume(musicSlider.getValue() / 200f);
            return false;
        });

        Image soundIcon = new Image(game.assetManager.get("textures/sound.png", Texture.class));
        soundIcon.setSize(20, 20);
        soundIcon.setPosition((STAGE_WIDTH - 150) / 2f - 29, 130);
        Slider soundSlider = SliderUtils.createSlider(game.assetManager, 150, 20, (STAGE_WIDTH - 150) / 2f, 130);
        Label soundLabel = SliderUtils.getValueLabel(game.assetManager, soundSlider, 150, 20, (STAGE_WIDTH - 150) / 2f,
                130);

        soundSlider.setValue(game.soundManager.getVolume() * 200);
        soundSlider.addListener(event -> {
            game.soundManager.setVolume(soundSlider.getValue() / 200f);
            return false;
        });

        stage.addActor(menuButton);
        stage.addActor(playButton);
        stage.addActor(restartButton);
        stage.addActor(musicIcon);
        stage.addActor(musicSlider);
        stage.addActor(musicLabel);
        stage.addActor(soundIcon);
        stage.addActor(soundSlider);
        stage.addActor(soundLabel);
    }
}
