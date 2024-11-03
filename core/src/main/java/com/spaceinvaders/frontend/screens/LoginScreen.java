package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
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
import com.spaceinvaders.frontend.utils.TextFieldUtils;

import java.io.IOException;

public class LoginScreen implements Screen {
    private final SpaceInvadersGame game;

    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Stage stage;

    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    private final Texture title;

    public LoginScreen(SpaceInvadersGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage(viewport);

        TextField idField = TextFieldUtils.createTextField("Enter id",93, 15, 73, 135 - 85);
        TextField passwordField = TextFieldUtils.createPasswordField(93, 15, 73, 135 - 101);
        ImageTextButton submitButton = ButtonUtils.createButton(
            game, "Submit", "textures/button.png", "textures/button.png", 93, 15, 73, 135 - 117, null);
        stage.addActor(idField);
        stage.addActor(passwordField);
        stage.addActor(submitButton);

        submitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String id = idField.getText();
                String password = passwordField.getText();
                try {
                    String token = ClientFirebase.signIn(id, password);
                }
                catch(AuthenticationException e) {
                    System.out.println("Incorrect username or password");
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
