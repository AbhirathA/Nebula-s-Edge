package com.spaceinvaders.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spaceinvaders.util.LoggerUtil;
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

    // Constants for the world and stage dimensions, in pixel-art pixels.
    // The server works in units 10x smaller (see server/src/main/resources/gameConstants.json).
    public static final float WORLD_WIDTH = 240;   // visible area of the game camera
    public static final float WORLD_HEIGHT = 135;
    public static final float STAGE_WIDTH = 480;   // UI layout (menus, buttons)
    public static final float STAGE_HEIGHT = 270;
    public static final float GAME_WIDTH = 1200;   // whole playfield; the camera follows the player around it
    public static final float GAME_HEIGHT = 675;

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
        email = "Guest";
        killCount = "0";
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        assetManager = new MyAssetManager();
        musicManager = new MusicManager();
        soundManager = new SoundManager();

        setScreen(new LoadingScreen(this, new CommandClass()));
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F12)) {
            saveScreenshot();
        }
    }

    // F12: saves the current frame to screenshots/ next to the assets folder
    private void saveScreenshot() {
        int width = Gdx.graphics.getBackBufferWidth();
        int height = Gdx.graphics.getBackBufferHeight();
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, width, height);
        try {
            FileHandle file = Gdx.files.local("../screenshots/nebulas-edge-" + System.currentTimeMillis() + ".png");
            PixmapIO.writePNG(file, pixmap, -1, true);
            LoggerUtil.logInfo("Saved screenshot " + file.file().getCanonicalPath());
        } catch (Exception e) {
            LoggerUtil.logError("Could not save screenshot: " + e.getMessage());
        } finally {
            pixmap.dispose();
        }
    }

    @Override
    public void dispose() {
        ScreenManager.getInstance(this).dispose();
        assetManager.dispose();
    }
}
