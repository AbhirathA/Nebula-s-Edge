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
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.backend.utils.UDPPacket;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.gameplay.GameplayStage;
import com.spaceinvaders.frontend.ui.UIStage;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameplayScreen implements Screen {
    private final SpaceInvadersGame game;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private UIStage uiStage;

    private GameplayStage gameplayStage;

    private UDPClient udpClient;
    private final UDPPacket udpPacket;

    private InputMultiplexer multiplexer;

    private final boolean isMulti;

    private float bulletCooldown = 0.25f; // Cooldown in seconds
    private float bulletTimer = 0;       // Timer to track elapsed time

    private AtomicBoolean hasReceived = new AtomicBoolean(false);

    public GameplayScreen(SpaceInvadersGame game, boolean isMulti) {
        this.game = game;
        this.isMulti = isMulti;

        camera = new OrthographicCamera();
        viewport = new FitViewport(SpaceInvadersGame.WORLD_WIDTH, SpaceInvadersGame.WORLD_HEIGHT, camera);
        viewport.apply();

        camera.setToOrtho(false);
        camera.position.set(SpaceInvadersGame.GAME_WIDTH/2, SpaceInvadersGame.GAME_HEIGHT/2, 0);
        camera.update();

        uiStage = new UIStage(game, new FitViewport(SpaceInvadersGame.WORLD_WIDTH, SpaceInvadersGame.WORLD_HEIGHT), isMulti);
        gameplayStage = new GameplayStage(game, viewport, SpaceInvadersGame.GAME_WIDTH, SpaceInvadersGame.GAME_HEIGHT, isMulti);

        this.udpPacket = new UDPPacket();
        this.udpClient = new UDPClient(this.udpPacket, this.hasReceived);

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

        // Update the bullet timer
        bulletTimer += delta;

        // Add BULLET state only if cooldown has passed
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && bulletTimer >= bulletCooldown) {
            state += "BULLET";
            game.soundManager.play("shoot");
            bulletTimer = 0; // Reset the timer
        }

        try {
            Thread.sleep(Math.max((int)(1000/144f) - ((int) delta * 1000L), 0));
        } catch (Exception e)  {
            e.printStackTrace();
        }

        this.udpClient.send(state, this.game.token);

        // use this object to render objects on screen
        UDPPacket tempUdpPacket = new UDPPacket();
        synchronized (this.udpPacket) {
            tempUdpPacket.update(this.udpPacket);
        }

        // set positions based on this.udpPacket
        // for now just implemented myShip,
        // TODO: need to implement for other ships

        boolean found = false;
        for(Coordinate coordinate : tempUdpPacket.spaceShips) {
            System.out.println(coordinate.id + " ");
            if(coordinate.getId() == tempUdpPacket.id) {
                this.gameplayStage.getRocketSprite().setPosition(coordinate.getX() - this.gameplayStage.getRocketSprite().getWidth() / 2f, coordinate.getY() -  this.gameplayStage.getRocketSprite().getHeight() / 2f);
                this.gameplayStage.getRocketSprite().setRotation(coordinate.getAngle());
                this.uiStage.getHealthBar().setHealth(coordinate.health);
                found = true;
                break;
            }
        }
        System.out.println();

        if (!found && this.hasReceived.get()) {
            this.game.screenManager.setScreen(ScreenState.GAME_OVER);
        }

        this.gameplayStage.setUdpPacket(tempUdpPacket);
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
        if(isMulti) {
            game.screenManager.setScreen(ScreenState.MULTIPLAYER_PAUSE);
        }
        else {
            game.screenManager.setScreen(ScreenState.SINGLEPLAYER_PAUSE);
        }
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

        camera.position.x = MathUtils.clamp(camera.position.x, SpaceInvadersGame.WORLD_WIDTH / 2, SpaceInvadersGame.GAME_WIDTH - SpaceInvadersGame.WORLD_WIDTH / 2);
        camera.position.y = MathUtils.clamp(camera.position.y, SpaceInvadersGame.WORLD_HEIGHT / 2, SpaceInvadersGame.GAME_HEIGHT - SpaceInvadersGame.WORLD_HEIGHT / 2);

        camera.update();
    }
}
