package com.spaceinvaders.frontend.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.backend.UDPClient;
import com.spaceinvaders.backend.utils.UDPPacket;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.gameplay.GameplayStage;
import com.spaceinvaders.frontend.ui.UIStage;

public class GameplayMultiplayerScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private float CAMERA_WIDTH;
    private float CAMERA_HEIGHT;

    private float WORLD_WIDTH;
    private float WORLD_HEIGHT;

    private UIStage uiStage;

    private GameplayStage gameplayStage;

    private UDPClient udpClient;
    private final UDPPacket udpPacket;

    private InputMultiplexer multiplexer;

    public GameplayMultiplayerScreen(SpaceInvadersGame game, float CAMERA_WIDTH, float CAMERA_HEIGHT, float WORLD_WIDTH, float WORLD_HEIGHT) {
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

        uiStage = new UIStage(game, new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT), ScreenState.MULTIPLAYER_PAUSE);
        gameplayStage = new GameplayStage(game, viewport, WORLD_WIDTH, WORLD_HEIGHT);

        this.udpPacket = new UDPPacket(gameplayStage.getRocketSprite().getX(), gameplayStage.getRocketSprite().getY(), gameplayStage.getRocketSprite().getRotation());
        this.udpClient = new UDPClient(udpPacket);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); // UI input comes first
        multiplexer.addProcessor(gameplayStage); // Gameplay input
        multiplexer.addProcessor(new InputAdapter() { // Gameplay-specific input
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    pause();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        uiStage.setPaused(false);

        this.udpClient.startReceiveThread(); // start thread to receive packets
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        this.viewport.apply();
        this.camera.update();

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.camera.combined);

        String state = "";
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            state += "FORWARD";
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            state += "BACKWARD";
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            state += "LEFT";
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            state += "RIGHT";
        }

//        try {
//            Thread.sleep(Math.max((int)(100/3) - ((int) delta * 1000L), 0));
//        } catch (Exception e)  {
//            e.printStackTrace();
//        }

        this.udpClient.send(state, this.game.token);

        // use this object to render objects on screen
        UDPPacket tempUdpPacket = new UDPPacket();
        synchronized (this.udpPacket) {
            tempUdpPacket.update(this.udpPacket);
        }

        // set positions based on this.udpPacket
        // for now just implemented myShip,
        // TODO: need to implement for other ships
        this.gameplayStage.getRocketSprite().setPosition(tempUdpPacket.myShip.x, tempUdpPacket.myShip.y);
        this.gameplayStage.getRocketSprite().setRotation(tempUdpPacket.myShip.angle);
        updateCamera();

        Gdx.gl.glEnable(GL20.GL_BLEND); // Enable blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        gameplayStage.act(delta);
        gameplayStage.draw();

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        this.viewport.update(i, i1);
    }

    @Override
    public void pause() {
        game.musicManager.pause();
        game.screenManager.setScreen(ScreenState.MULTIPLAYER_PAUSE);
        uiStage.setPaused(true);
    }

    @Override
    public void resume() {
        game.musicManager.resume();
        uiStage.setPaused(false);
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        uiStage.setPaused(true);
        this.udpClient.receiveThread.interrupt();
    }

    @Override
    public void dispose() {
    }

    private void updateCamera() {
        camera.position.set(gameplayStage.getRocketSprite().getX() + gameplayStage.getRocketSprite().getWidth() / 2, gameplayStage.getRocketSprite().getY() + gameplayStage.getRocketSprite().getHeight() / 2, 0);

        camera.position.x = MathUtils.clamp(camera.position.x, CAMERA_WIDTH / 2, WORLD_WIDTH - CAMERA_WIDTH / 2);
        camera.position.y = MathUtils.clamp(camera.position.y, CAMERA_HEIGHT / 2, WORLD_HEIGHT - CAMERA_HEIGHT / 2);

        camera.update();
    }
}
