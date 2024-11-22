package com.spaceinvaders.frontend.ui;

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

public class UIStage extends Stage {
    private HealthBar healthBar;
    private Label timerLabel; // Timer label to display the countdown
    private float timeRemaining; // Time remaining for the countdown
    private boolean isPaused = false;

    public UIStage(SpaceInvadersGame game, Viewport viewport) {
        super(viewport, game.batch);

        // Initialize health bar and add to stage
        healthBar = new HealthBar(game.assetManager, 2, viewport.getWorldHeight() - 11, 10);
        addActor(healthBar);

        // Create pause button and add to stage
        ImageButton pauseButton = ButtonUtils.createScreenNavigationButton(game, "textures/pause.png", "textures/pause.png", 7, 7, viewport.getWorldWidth() - 10, viewport.getWorldHeight() - 10, ScreenState.PAUSE);
        addActor(pauseButton);

        // Initialize and add timer
        initializeTimer(game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class), viewport.getWorldWidth(), viewport.getWorldHeight(), 60); // Start a 60-second timer
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    /**
     * Initializes and starts a countdown timer.
     *
     * @param font          The font for the timer label.
     * @param viewportWidth The width of the viewport.
     * @param viewportHeight The height of the viewport.
     * @param initialTime   The starting time for the timer in seconds.
     */
    private void initializeTimer(BitmapFont font, float viewportWidth, float viewportHeight, float initialTime) {
        // Initialize timer label with the initial time
        timeRemaining = initialTime;
        timerLabel = LabelUtils.createLabel(convertSecondsToTimeString(timeRemaining), font);
        timerLabel.setPosition((viewportWidth - timerLabel.getWidth()) / 2, viewportHeight - 10);
        addActor(timerLabel);

        // Schedule timer updates
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!isPaused) { // Only update if not paused
                    timeRemaining--;

                    // Update the timer label
                    timerLabel.setText(convertSecondsToTimeString(timeRemaining));

                    // Stop the timer when time runs out
                    if (timeRemaining <= 0) {
                        cancel();
                    }
                }
            }
        }, 1, 1); // Schedule the task to run every second after an initial delay of 1 second
    }

    private String convertSecondsToTimeString(float seconds) {
        int totalSeconds = Math.round(seconds);
        int minutes = totalSeconds / 60; // Get minutes
        int remainingSeconds = totalSeconds % 60; // Get remaining seconds

        // Format as MM:SS
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
