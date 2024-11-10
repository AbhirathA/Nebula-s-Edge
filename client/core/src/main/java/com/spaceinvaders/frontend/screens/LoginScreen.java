package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.backend.firebase.AuthenticationException;
import com.spaceinvaders.backend.firebase.ClientFirebase;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;
import com.spaceinvaders.frontend.utils.TextFieldUtils;

import java.io.IOException;

public class LoginScreen implements Screen {
    private final SpaceInvadersGame game;

    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;

    private final float STAGE_WIDTH = WORLD_WIDTH * 3/2;
    private final float STAGE_HEIGHT = WORLD_HEIGHT * 3/2;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Viewport stageViewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private final Texture title;

    boolean isErrorDisplayed = false;
    boolean isLoggedIn = false;

    public LoginScreen(SpaceInvadersGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stageViewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT);

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(stageViewport);

        initialiseActors();

        starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 30);
        planetsBackground = new PlanetsBackground(game.assetManager);

        title = game.assetManager.get("textures/title.png", Texture.class);
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

        stageViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i, i1);
        stageViewport.update(i, i1);
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

    private void initialiseActors() {
        Label errorMessage = LabelUtils.createLabel("Incorrect username or password", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), 0, 0);
        Label successMessage = LabelUtils.createLabel("Login successful", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), 0, 0);

        Label enterId = LabelUtils.createLabel("Id:", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), (STAGE_WIDTH - 143) / 2f, 86);
        Label enterPassword = LabelUtils.createLabel("Password:", game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), (STAGE_WIDTH - 143) / 2f, 70);
        TextField idField = TextFieldUtils.createTextField("Enter id", game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 84);
        TextField passwordField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 68);

        ImageTextButton submitButton = ButtonUtils.createButton(game, "Submit", "textures/button.png", "textures/button.png", 95, 15, (STAGE_WIDTH - 95) / 2f, 50);
        ImageTextButton signUpButton = ButtonUtils.createButton(game, "SignUp", "textures/button.png", "textures/button.png", 95, 15, (STAGE_WIDTH - 95) / 2f, 34);

        stage.addActor(enterId);
        stage.addActor(enterPassword);
        stage.addActor(idField);
        stage.addActor(passwordField);
        stage.addActor(submitButton);
        stage.addActor(signUpButton);

        signUpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.screenManager.setScreen(ScreenState.MAIN_MENU);
                return true;
            }
        });

        submitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String id = idField.getText();
                String password = passwordField.getText();
                try {
                    String token = ClientFirebase.signIn(id, password);

                    if (isErrorDisplayed) {
                        stage.getActors().removeValue(errorMessage, true);
                        isErrorDisplayed = false;

                        if (isLoggedIn) {
                            stage.getActors().removeValue(successMessage, true);
                            isLoggedIn = false;
                        }
                    }
                    if (!isLoggedIn) {
                        stage.addActor(successMessage);
                        isLoggedIn = true;
                        game.screenManager.setScreen(ScreenState.MAIN_MENU);
                    }
                }
                catch(AuthenticationException e) {
                    System.out.println("Incorrect username or password");
                    if (!isErrorDisplayed) {
                        stage.addActor(errorMessage);
                        isErrorDisplayed = true;

                        if (isLoggedIn) {
                            stage.getActors().removeValue(successMessage, true);
                        }
                    }
                }
                catch(IOException e) {
                    System.out.println("Could not connect to the server. Are you connected to the internet?");
                }
                catch(Exception e) {
                    //@TODO: Convert to logging
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
                return true;
            }
        });
    }
}
