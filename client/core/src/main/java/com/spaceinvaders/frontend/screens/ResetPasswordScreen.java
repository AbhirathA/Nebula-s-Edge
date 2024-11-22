package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.backend.firebase.AuthenticationManager;
import com.spaceinvaders.backend.firebase.utils.AuthenticationException;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;
import com.spaceinvaders.frontend.utils.TextFieldUtils;

public class ResetPasswordScreen implements Screen {
    private final SpaceInvadersGame game;

    private final float STAGE_WIDTH;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private final Texture title;

    private Label errorMessage;
    private Label successMessage;

    public ResetPasswordScreen(SpaceInvadersGame game, float WORLD_WIDTH, float WORLD_HEIGHT, float STAGE_WIDTH,
                        float STAGE_HEIGHT, StarsBackground starsBackground, PlanetsBackground planetsBackground) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        Viewport stageViewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT);

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(stageViewport);
        this.STAGE_WIDTH = STAGE_WIDTH;

        initialiseActors();

        this.starsBackground = starsBackground;
        this.planetsBackground = planetsBackground;

        title = game.assetManager.get("textures/title.png", Texture.class);
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
            (STAGE_WIDTH - 143) / 2f, 101);

        Label enterPassword = LabelUtils.createLabel("Password:",
            minecraftFont, (STAGE_WIDTH - 143) / 2f, 85);

        Label confirmPassword = LabelUtils.createLabel("Confirm:",
            minecraftFont, (STAGE_WIDTH - 143) / 2f, 68);

        TextField idField = TextFieldUtils.createTextField("", game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22,
            99);
        idField.setMessageText("Enter id");
        // Placeholder text will look gray without the following line
        idField.getStyle().messageFontColor = idField.getStyle().fontColor;

        TextField passwordField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15,
            (STAGE_WIDTH - 95) / 2f + 22, 83);
        passwordField.setMessageText("Enter password");
        passwordField.getStyle().messageFontColor = passwordField.getStyle().fontColor;

        TextField confirmField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15,
            (STAGE_WIDTH - 95) / 2f + 22, 66);
        confirmField.setMessageText("Confirm password");
        confirmField.getStyle().messageFontColor = confirmField.getStyle().fontColor;

        ImageTextButton submitButton = ButtonUtils.createButton(game, "Submit", "textures/button.png",
            "textures/button.png", 95, 15, (STAGE_WIDTH - 95) / 2f, 49);

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

                    if (ResetPasswordScreen.this.stage.getActors().contains(ResetPasswordScreen.this.errorMessage, true)) {
                        ResetPasswordScreen.this.stage.getActors().removeValue(ResetPasswordScreen.this.errorMessage, true);
                    }

                    if (!ResetPasswordScreen.this.stage.getActors().contains(ResetPasswordScreen.this.successMessage, true)) {
                        ResetPasswordScreen.this.stage.addActor(ResetPasswordScreen.this.successMessage);
                        ResetPasswordScreen.this.game.screenManager.setScreen(ScreenState.MAIN_MENU);
                    }

                } catch (AuthenticationException e) {
                    // @TODO: Convert to logging
                    System.out.println(e.getMessage());
                    ResetPasswordScreen.this.errorMessage.setText(e.getMessage());

                    if (!ResetPasswordScreen.this.stage.getActors().contains(ResetPasswordScreen.this.successMessage, true)) {
                        ResetPasswordScreen.this.stage.getActors().removeValue(ResetPasswordScreen.this.successMessage, true);
                    }

                    if (!ResetPasswordScreen.this.stage.getActors().contains(ResetPasswordScreen.this.errorMessage, true)) {
                        ResetPasswordScreen.this.stage.addActor(ResetPasswordScreen.this.errorMessage);
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
