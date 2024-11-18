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
import com.spaceinvaders.util.AuthenticationException;
import com.spaceinvaders.backend.firebase.ClientFirebase;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;
import com.spaceinvaders.frontend.utils.TextFieldUtils;

import java.io.IOException;

public class LoginScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Viewport stageViewport;

    private final Stage stage;
    private final float STAGE_WIDTH;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private final Texture title;

    boolean isErrorDisplayed = false;
    boolean isLoggedIn = false;

    public LoginScreen(SpaceInvadersGame game, float WORLD_WIDTH, float WORLD_HEIGHT, float STAGE_WIDTH, float STAGE_HEIGHT, StarsBackground starsBackground, PlanetsBackground planetsBackground) {
        this.game = game;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, this.camera);
        this.stageViewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT);

        this.camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        this.camera.update();

        this.stage = new Stage(this.stageViewport);
        this.STAGE_WIDTH = STAGE_WIDTH;

        this.initialiseActors();

        this.starsBackground = starsBackground;
        this.planetsBackground = planetsBackground;

        this.title = game.assetManager.get("textures/title.png", Texture.class);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0,1);

        this.viewport.apply();
        this.camera.update();

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);  // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.starsBackground.render(this.game.shapeRenderer, delta);  // Use delta for time
        this.planetsBackground.render(this.game.batch);

        this.game.batch.begin();
        this.game.batch.draw(title, 73, 135 - 66, 93, 47);
        this.game.batch.end();

        this.stageViewport.apply();
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        this.viewport.update(i, i1);
        this.stageViewport.update(i, i1);
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
        Label errorMessage = LabelUtils.createLabel("Incorrect username or password", this.game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), 0, 0);
        Label successMessage = LabelUtils.createLabel("Login successful", this.game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), 0, 0);

        Label enterId = LabelUtils.createLabel("Id:", this.game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), (STAGE_WIDTH - 143) / 2f, 86);
        Label enterPassword = LabelUtils.createLabel("Password:", this.game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), (STAGE_WIDTH - 143) / 2f, 70);
        TextField idField = TextFieldUtils.createTextField("Enter id", this.game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 84);
        TextField passwordField = TextFieldUtils.createPasswordField(this.game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 68);

        ImageTextButton submitButton = ButtonUtils.createButton(this.game, "Submit", "textures/button.png", "textures/button.png", 95, 15, (STAGE_WIDTH - 95) / 2f, 50);
        ImageTextButton signUpButton = ButtonUtils.createButton(this.game, "SignUp", "textures/button.png", "textures/button.png", 95, 15, (STAGE_WIDTH - 95) / 2f, 34);

        ImageButton backButton = ButtonUtils.createBackButton(this.game, "textures/back-button.png", "textures/back-button.png", 28, 15, 10, 177, game.screenManager.getRecentScreen());

        this.stage.addActor(enterId);
        this.stage.addActor(enterPassword);
        this.stage.addActor(idField);
        this.stage.addActor(passwordField);
        this.stage.addActor(submitButton);
        this.stage.addActor(signUpButton);
        this.stage.addActor(backButton);

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
                catch(AuthenticationException e)
                {
                    //@TODO: Convert to logging
                    System.out.println("Incorrect username or password");
                    if (!isErrorDisplayed)
                    {
                        stage.addActor(errorMessage);
                        isErrorDisplayed = true;

                        if (isLoggedIn)
                        {
                            stage.getActors().removeValue(successMessage, true);
                        }
                    }
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
