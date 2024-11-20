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
import com.spaceinvaders.backend.firebase.utils.AuthenticationException;
import com.spaceinvaders.backend.firebase.ClientFirebase;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;
import com.spaceinvaders.frontend.utils.TextFieldUtils;

public class SignupScreen implements Screen {
    private final SpaceInvadersGame game;

    private final float STAGE_WIDTH;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Viewport stageViewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private Label errorMessage;
    private Label successMessage;

    private final Texture title;

    public SignupScreen(SpaceInvadersGame game, float WORLD_WIDTH, float WORLD_HEIGHT, float STAGE_WIDTH,
            float STAGE_HEIGHT, StarsBackground starsBackground, PlanetsBackground planetsBackground) {
        this.game = game;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.stageViewport = new FitViewport(STAGE_WIDTH, STAGE_HEIGHT);

        this.camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        this.camera.update();

        this.stage = new Stage(stageViewport);
        this.STAGE_WIDTH = STAGE_WIDTH;

        this.initialiseActors();

        this.starsBackground = starsBackground;
        this.planetsBackground = planetsBackground;

        this.title = game.assetManager.get("textures/title.png", Texture.class);
    }

    @Override
    public void show() {
        this.clearErrorAndSuccessLabel();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.viewport.apply();
        this.camera.update();

        this.game.batch.setProjectionMatrix(camera.combined);
        this.game.shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND); // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.starsBackground.render(game.shapeRenderer, delta); // Use delta for time
        this.planetsBackground.render(game.batch);

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

    private void clearErrorAndSuccessLabel() {
        this.stage.getActors().removeValue(this.errorMessage, true);
        this.stage.getActors().removeValue(this.successMessage, true);
    }

    private void initialiseActors() {

        BitmapFont minecraftFont = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);
        this.errorMessage = LabelUtils.createLabel("Incorrect username or password", minecraftFont, 0, 0);
        this.successMessage = LabelUtils.createLabel("Login successful", minecraftFont, 0, 0);
        Label enterId = LabelUtils.createLabel("Id:", minecraftFont, (STAGE_WIDTH - 143) / 2f, 86);
        Label enterPassword = LabelUtils.createLabel("Password:", minecraftFont, (STAGE_WIDTH - 143) / 2f, 70);
        Label confirmPassword = LabelUtils.createLabel("Confirm:", minecraftFont, (STAGE_WIDTH - 143) / 2f, 53);
        TextField idField = TextFieldUtils.createTextField("", game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 84);
        idField.setMessageText("Enter id");
        // Placeholder text will look gray without the following line
        idField.getStyle().messageFontColor = idField.getStyle().fontColor;

        TextField passwordField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 68);
        passwordField.setMessageText("Enter password");
        passwordField.getStyle().messageFontColor = passwordField.getStyle().fontColor;

        TextField confirmField = TextFieldUtils.createPasswordField(game.assetManager, 95, 15, (STAGE_WIDTH - 95) / 2f + 22, 51);
        confirmField.setMessageText("Confirm password");
        confirmField.getStyle().messageFontColor = confirmField.getStyle().fontColor;

        ImageTextButton submitButton = ButtonUtils.createButton(game, "Submit", "textures/button.png", "textures/button.png", 95, 15, (STAGE_WIDTH - 95) / 2f, 34);
        ImageButton backButton = ButtonUtils.createBackButton(this.game, "textures/back-button.png", "textures/back-button.png", 28, 15, 10, 177, game.screenManager.getRecentScreen());

        this.stage.addActor(enterId);
        this.stage.addActor(enterPassword);
        this.stage.addActor(confirmPassword);
        this.stage.addActor(idField);
        this.stage.addActor(passwordField);
        this.stage.addActor(confirmField);
        this.stage.addActor(submitButton);
        this.stage.addActor(backButton);

        submitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String id = idField.getText();
                String password = passwordField.getText();
                String confirm = confirmField.getText();
                try {
                    ClientFirebase.signUp(id, password, confirm);

                    if (SignupScreen.this.stage.getActors().contains(SignupScreen.this.errorMessage, true)) {
                        SignupScreen.this.stage.getActors().removeValue(SignupScreen.this.errorMessage, true);
                    }

                    if (SignupScreen.this.stage.getActors().contains(SignupScreen.this.successMessage, true)) {
                        SignupScreen.this.stage.addActor(SignupScreen.this.successMessage);
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
