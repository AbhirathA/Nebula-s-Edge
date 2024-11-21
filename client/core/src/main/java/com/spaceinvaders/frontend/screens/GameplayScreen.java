package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.spaceinvaders.frontend.ui.UIStage;

public class GameplayScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private float CAMERA_WIDTH;
    private float CAMERA_HEIGHT;

    private float WORLD_WIDTH;
    private float WORLD_HEIGHT;

    private final StarsBackground starsBackground;
    private final Sprite rocketSprite;

    private UIStage uiStage;

    public GameplayScreen(SpaceInvadersGame game, float CAMERA_WIDTH, float CAMERA_HEIGHT, float WORLD_WIDTH, float WORLD_HEIGHT) {
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

        starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 200);
        Texture rocket = game.assetManager.get("textures/Rocket3.png", Texture.class);
        rocketSprite = new Sprite(rocket);
        rocketSprite.setPosition(WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f);
        rocketSprite.setSize(21, 21);
        rocketSprite.setOrigin(rocketSprite.getWidth() / 2, rocketSprite.getHeight() / 2);

        uiStage = new UIStage(game, new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT));
    }
    @Override
    public void show() {
        game.musicManager.play("gameplay");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.viewport.apply();
        this.camera.update();

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rocketSprite.rotate(1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rocketSprite.rotate(-1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveInDirection(1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveInDirection(-1);
        }

        updateCamera();

        Gdx.gl.glEnable(GL20.GL_BLEND); // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.starsBackground.render(this.game.shapeRenderer, delta);

        game.batch.begin();
        rocketSprite.draw(game.batch);
        game.batch.end();

        uiStage.act(delta);
        uiStage.draw();

        if(Gdx.input.justTouched()) {
            uiStage.getHealthBar().changeHealth(-1);
            game.soundManager.play("shoot");
        }
    }

    @Override
    public void resize(int i, int i1) {
        this.viewport.update(i, i1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    private void moveInDirection(float speed) {
        float rotation = rocketSprite.getRotation();

        float angleRad = (float) Math.toRadians(rotation) + 1.571f;

        float deltaX = MathUtils.cos(angleRad) * speed;
        float deltaY = MathUtils.sin(angleRad) * speed;

        rocketSprite.translate(deltaX, deltaY);

        rocketSprite.setX(MathUtils.clamp(rocketSprite.getX(), 0, WORLD_WIDTH - rocketSprite.getWidth()));
        rocketSprite.setY(MathUtils.clamp(rocketSprite.getY(), 0, WORLD_HEIGHT - rocketSprite.getHeight()));
    }

    private void updateCamera() {
        camera.position.set(rocketSprite.getX(), rocketSprite.getY(), 0);

        camera.position.x = MathUtils.clamp(camera.position.x, CAMERA_WIDTH / 2, WORLD_WIDTH - CAMERA_WIDTH / 2);
        camera.position.y = MathUtils.clamp(camera.position.y, CAMERA_HEIGHT / 2, WORLD_HEIGHT - CAMERA_HEIGHT / 2);

        camera.update();
    }
}
