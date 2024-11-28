package com.spaceinvaders.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spaceinvaders.backend.firebase.utils.ServerInfo;
import com.spaceinvaders.frontend.managers.MusicManager;
import com.spaceinvaders.frontend.managers.ScreenManager;
import com.spaceinvaders.frontend.managers.MyAssetManager;
import com.spaceinvaders.frontend.managers.SoundManager;
import com.spaceinvaders.frontend.screens.LoadingScreen;
import com.spaceinvaders.frontend.screens.ScreenState;
import com.spaceinvaders.frontend.utils.Command;

public class SpaceInvadersGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;

    public MyAssetManager assetManager;
    public ScreenManager screenManager;
    public MusicManager musicManager;
    public SoundManager soundManager;

    public String token;
    public String email;
    public String killCount;

    // Constants for the world and stage dimensions
    public static final float WORLD_WIDTH = ServerInfo.getClientConstants().get("WORLD_WIDTH").getAsFloat();
    public static final float WORLD_HEIGHT = ServerInfo.getClientConstants().get("WORLD_HEIGHT").getAsFloat();
    public static final float STAGE_WIDTH = ServerInfo.getClientConstants().get("STAGE_WIDTH").getAsFloat();
    public static final float STAGE_HEIGHT = ServerInfo.getClientConstants().get("STAGE_HEIGHT").getAsFloat();
    public static final float GAME_WIDTH = ServerInfo.getClientConstants().get("GAME_WIDTH").getAsFloat();
    public static final float GAME_HEIGHT = ServerInfo.getClientConstants().get("GAME_HEIGHT").getAsFloat();

    private class CommandClass implements Command {

        @Override
        public void execute() {
            SpaceInvadersGame.this.assetManager.loadAssets();
        }

        @Override
        public void onUpdate() {
            SpaceInvadersGame.this.musicManager.loadMusic(SpaceInvadersGame.this.assetManager);
            SpaceInvadersGame.this.soundManager.loadSounds(SpaceInvadersGame.this.assetManager);
            SpaceInvadersGame.this.screenManager = new ScreenManager(SpaceInvadersGame.this);
            SpaceInvadersGame.this.screenManager.setScreen(ScreenState.LOGIN_GATEWAY);
        }

        @Override
        public boolean update() {
            return SpaceInvadersGame.this.assetManager.update();
        }
    }

    @Override
    public void create() {
        token = "";
        email = "";
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        assetManager = new MyAssetManager();
        musicManager = new MusicManager();
        soundManager = new SoundManager();

        setScreen(new LoadingScreen(this, new CommandClass()));
    }

    @Override
    public void dispose() {
        ScreenManager.getInstance(this).dispose();
        assetManager.dispose();
    }
}
