package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.screens.ScreenState;
import com.spaceinvaders.frontend.utils.ButtonUtils;
import com.spaceinvaders.frontend.utils.LabelUtils;

/**
 * UIStage is a custom Stage that contains UI elements for the Space Invaders game,
 * such as a health bar, a pause button, and a countdown timer.
 */
public class UIStage extends Stage {
    private SpaceInvadersGame game;
    private HealthBar healthBar;
    private GameOver gameOver;
    private Victory victory;
    private Label timerLabel;
    private float timeRemaining;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private final boolean isMulti;

    /**
     * Constructor for UIStage.
     *
     * @param game     The SpaceInvadersGame instance.
     * @param viewport The viewport used to display the stage.
     */
    public UIStage(SpaceInvadersGame game, Viewport viewport, boolean isMulti) {
        super(viewport, game.batch);
        this.game = game;
        this.isMulti = isMulti;

        // Initialize health bar and add it to the stage
        healthBar = new HealthBar(game.assetManager, 2, viewport.getWorldHeight() - 11, 10);
        addActor(healthBar);

        // Initialize Game over screen
        gameOver = new GameOver(game);

        // Initialize Victory screen
        victory = new Victory(game);

        ScreenState screenState;
        if(isMulti) {
            screenState = ScreenState.MULTIPLAYER_PAUSE;
        } else {
            screenState = ScreenState.SINGLEPLAYER_PAUSE;
        }

        // Create and add a pause button to the stage
        ImageButton pauseButton = ButtonUtils.createScreenNavigationButton( game, "textures/pause.png", "textures/pause.png", 7, 7, viewport.getWorldWidth() - 10, viewport.getWorldHeight() - 10, screenState);
        addActor(pauseButton);

        // Initialize and start the countdown timer
        if(!isMulti) {
            initializeTimer(game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), viewport.getWorldWidth(), viewport.getWorldHeight(), 60);  // Start a 60-second timer
        }
    }

    @Override
    public void act(float delta) {
        if(Gdx.input.justTouched()) {
//            healthBar.changeHealth(-1);
            game.soundManager.play("shoot");
        }

        if(!isMulti && healthBar.getCurrentHealth() == 0 && !isGameOver) {
            addGameOver();
            isPaused = true;
            isGameOver = true;
        }

        if(!isMulti && timeRemaining == 0 && !isGameOver) {
            addVictory();
            isPaused = true;
            isGameOver = true;
        }

        super.act(delta);
    }

    /**
     * Gets the health bar associated with the UIStage.
     *
     * @return The HealthBar instance.
     */
    public HealthBar getHealthBar() {
        return healthBar;
    }

    /**
     * Initializes and starts a countdown timer.
     *
     * @param font           The font used to display the timer.
     * @param viewportWidth  The width of the viewport.
     * @param viewportHeight The height of the viewport.
     * @param initialTime    The starting time for the timer in seconds.
     */
    private void initializeTimer(BitmapFont font, float viewportWidth, float viewportHeight, float initialTime) {
        // Set the initial countdown time
        timeRemaining = initialTime;

        // Create the timer label with the initial time formatted as MM:SS
        timerLabel = LabelUtils.createLabel(convertSecondsToTimeString(timeRemaining), font);
        timerLabel.setPosition((viewportWidth - timerLabel.getWidth()) / 2, viewportHeight - 10); // Center the label at the top of the screen
        addActor(timerLabel);

        // Schedule a task to update the timer every second
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isPaused) { // Update the timer only if the game is not paused
                    timeRemaining--; // Decrease the remaining time

                    // Update the timer label with the new time
                    timerLabel.setText(convertSecondsToTimeString(timeRemaining));

                    // Stop the timer when the countdown reaches zero
                    if (timeRemaining <= 0) {
                        cancel();
                    }
                }
            }
        }, 1, 1); // Start after 1 second and repeat every 1 second
    }

    /**
     * Converts a time value in seconds to a string formatted as MM:SS.
     *
     * @param seconds The time in seconds.
     * @return A string formatted as MM:SS.
     */
    private String convertSecondsToTimeString(float seconds) {
        int totalSeconds = Math.round(seconds);
        int minutes = totalSeconds / 60; // Calculate minutes
        int remainingSeconds = totalSeconds % 60; // Calculate remaining seconds

        // Return the formatted time string
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * Sets the paused state of the game.
     *
     * @param paused True to pause the game, false to resume.
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }

    public void addGameOver() {
        addActor(gameOver);
        game.musicManager.pause();
        game.soundManager.play("gameOver");
    }

    public void addVictory() {
        addActor(victory);
        game.musicManager.pause();
        game.soundManager.play("victory");
    }
}
