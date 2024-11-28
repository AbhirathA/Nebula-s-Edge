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
import com.spaceinvaders.backend.firebase.AuthenticationManager;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.backend.firebase.utils.AuthenticationException;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.ui.LoadingRing;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;
import com.spaceinvaders.frontend.utils.TextFieldUtils;

public class SignupScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private final Texture title;

    private Label errorMessage;
    private Label successMessage;

    private LoadingRing loadingRing;

    public SignupScreen(SpaceInvadersGame game, StarsBackground starsBackground, PlanetsBackground planetsBackground) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(SpaceInvadersGame.WORLD_WIDTH, SpaceInvadersGame.WORLD_HEIGHT, camera);
        Viewport stageViewport = new FitViewport(SpaceInvadersGame.STAGE_WIDTH, SpaceInvadersGame.STAGE_HEIGHT);

        camera.position.set(SpaceInvadersGame.WORLD_WIDTH / 2, SpaceInvadersGame.WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(stageViewport);

        initialiseActors();

        this.starsBackground = starsBackground;
        this.planetsBackground = planetsBackground;

        title = game.assetManager.get("textures/title.png", Texture.class);
        this.loadingRing = new LoadingRing(game.assetManager, SpaceInvadersGame.STAGE_WIDTH / 2 - 10, SpaceInvadersGame.STAGE_HEIGHT / 2 - 30);
    }

    @Override
    public void show() {
        game.musicManager.play("introMusic");
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
        planetsBackground.render(game.batch);

        game.batch.begin();
        game.batch.draw(title, 73, 61, 93, 47);
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

    private void initialiseActors() {

        BitmapFont minecraftFont = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);

        errorMessage = LabelUtils.createLabel("Incorrect username or password",
                minecraftFont, 0, 0);

        successMessage = LabelUtils.createLabel("Login successful",
                minecraftFont, 0, 0);

        Label enterId = LabelUtils.createLabel("Id:", minecraftFont,
                (SpaceInvadersGame.STAGE_WIDTH - 143) / 2f, 101);

        Label enterPassword = LabelUtils.createLabel("Password:",
                minecraftFont, (SpaceInvadersGame.STAGE_WIDTH - 143) / 2f, 85);

        Label confirmPassword = LabelUtils.createLabel("Confirm:",
                minecraftFont, (SpaceInvadersGame.STAGE_WIDTH - 143) / 2f, 68);

        TextField idField = TextFieldUtils.createTextField("", game.assetManager, 95, 15, (SpaceInvadersGame.STAGE_WIDTH - 95) / 2f + 22,
                99);
        idField.setMessageText("Enter id");
        // Placeholder text will look gray without the following line
        idField.getStyle().messageFontColor = idField.getStyle().fontColor;

        TextField passwordField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15,
                (SpaceInvadersGame.STAGE_WIDTH - 95) / 2f + 22, 83);
        passwordField.setMessageText("Enter password");
        passwordField.getStyle().messageFontColor = passwordField.getStyle().fontColor;

        TextField confirmField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15,
                (SpaceInvadersGame.STAGE_WIDTH - 95) / 2f + 22, 66);
        confirmField.setMessageText("Confirm password");
        confirmField.getStyle().messageFontColor = confirmField.getStyle().fontColor;

        ImageTextButton submitButton = ButtonUtils.createButton(game, "Submit", "textures/button.png",
                "textures/button.png", 95, 15, (SpaceInvadersGame.STAGE_WIDTH - 95) / 2f, 49);

        ImageButton backButton = ButtonUtils.createBackButton(this.game, "textures/back-button.png",
                "textures/back-button.png", 28, 15, 10, 245, game.screenManager.getRecentScreen());

        stage.addActor(enterId);
        stage.addActor(enterPassword);
        stage.addActor(confirmPassword);
        stage.addActor(idField);
        stage.addActor(passwordField);
        stage.addActor(confirmField);
        stage.addActor(submitButton);
        stage.addActor(backButton);

        submitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String id = idField.getText();
                String password = passwordField.getText();
                String confirm = confirmField.getText();
                try {
                    AuthenticationManager.signUp(id, password, confirm);

                    if (SignupScreen.this.stage.getActors().contains(SignupScreen.this.errorMessage, true)) {
                        SignupScreen.this.stage.getActors().removeValue(SignupScreen.this.errorMessage, true);
                    }

                    if (!SignupScreen.this.stage.getActors().contains(SignupScreen.this.successMessage, true)) {
                        SignupScreen.this.stage.addActor(SignupScreen.this.successMessage);
                        idField.setMessageText("Enter id");
                        passwordField.setMessageText("Enter password");
                        confirmField.setMessageText("Confirm password");
                        SignupScreen.this.game.screenManager.setScreen(ScreenState.LOGIN);
                    }

                } catch (AuthenticationException e) {
                    // @TODO: Convert to logging
                    System.out.println(e.getMessage());
                    SignupScreen.this.errorMessage.setText(e.getMessage());

                    if (!SignupScreen.this.stage.getActors().contains(SignupScreen.this.successMessage, true)) {
                        SignupScreen.this.stage.getActors().removeValue(SignupScreen.this.successMessage, true);
                    }

                    if (!SignupScreen.this.stage.getActors().contains(SignupScreen.this.errorMessage, true)) {
                        SignupScreen.this.stage.addActor(SignupScreen.this.errorMessage);
                    }

                } catch (Exception e) {
                    // @TODO: Convert to logging
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
                return true;
            }
        });
    }
}
