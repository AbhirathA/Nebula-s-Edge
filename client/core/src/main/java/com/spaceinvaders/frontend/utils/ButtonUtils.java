package com.spaceinvaders.frontend.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.screens.ScreenState;

/**
 * Utility class for creating buttons in a LibGDX application.
 * Provides methods to create and customize labels with different parameters.
 */
public class ButtonUtils {

    /**
     * Creates an ImageTextButton for navigating between screens.
     *
     * @param game          Reference to the SpaceInvadersGame instance.
     * @param buttonText    Text to display on the button.
     * @param upImagePath   Path to the button's "up" (default) image.
     * @param downImagePath Path to the button's "down" (pressed) image.
     * @param width         Width of the button.
     * @param height        Height of the button.
     * @param x             X position of the button.
     * @param y             Y position of the button.
     * @param targetScreen  Screen to navigate to when the button is clicked.
     * @return              Configured ImageTextButton instance.
     */
    public static ImageTextButton createScreenNavigationButton(SpaceInvadersGame game, String buttonText, String upImagePath, String downImagePath, float width, float height, float x, float y, ScreenState targetScreen) {
        // Load textures for button states
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        // Create drawable objects for button states
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        // Define the style for the button
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        // Set font and font color for button text
        BitmapFont font = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);
        style.font = font;
        style.fontColor = Color.valueOf("4b692f");

        // Create and configure the ImageTextButton
        ImageTextButton button = new ImageTextButton(buttonText, style);
        button.setSize(width, height);
        button.setPosition(x, y);

        // Add input listener for navigation and effects
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Navigate to the next Screen
                if(targetScreen == null){
                    System.out.println("Move to new screen");
                }
                else {
                    game.screenManager.setScreen(targetScreen);
                }
                return true;
            }

            // Change cursor to hand icon when hovering over the button
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            // Reset cursor to default arrow when exiting the button area
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return button;
    }

    /**
     * Creates an ImageButton for navigating between screens.
     *
     * @param game          Reference to the SpaceInvadersGame instance.
     * @param upImagePath   Path to the button's "up" (default) image.
     * @param downImagePath Path to the button's "down" (pressed) image.
     * @param width         Width of the button.
     * @param height        Height of the button.
     * @param x             X position of the button.
     * @param y             Y position of the button.
     * @param targetScreen  Screen to navigate to when the button is clicked.
     * @return              Configured ImageButton instance.
     */
    public static ImageButton createScreenNavigationButton(SpaceInvadersGame game, String upImagePath, String downImagePath, float width, float height, float x, float y, ScreenState targetScreen) {
        // Load textures for button states
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        // Create drawables from textures
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        // Create the button style
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        // Create the ImageButton with the defined style
        ImageButton button = new ImageButton(style);
        button.setSize(width, height);
        button.setPosition(x, y);

        // Add input listener for navigation and effects
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Navigate to the next Screen
                if(targetScreen == null){
                    System.out.println(targetScreen);
                    System.out.println("Move to new screen");
                }
                else {
                    game.screenManager.setScreen(targetScreen);
                }
                return true;
            }

            // Change cursor to hand icon when hovering over the button
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            // Reset cursor to default arrow when exiting the button area
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return button;
    }

    /**
     * Creates a BackButton for navigating to the last screen.
     *
     * @param game          Reference to the SpaceInvadersGame instance.
     * @param upImagePath   Path to the button's "up" (default) image.
     * @param downImagePath Path to the button's "down" (pressed) image.
     * @param width         Width of the button.
     * @param height        Height of the button.
     * @param x             X position of the button.
     * @param y             Y position of the button.
     * @param nextState     Screen to navigate to when the button is clicked.
     * @return              An ImageButton instance configured as a back button.
     */
    public static ImageButton createBackButton(SpaceInvadersGame game, String upImagePath, String downImagePath, float width, float height, float x, float y, ScreenState nextState) {
        // Load textures for the button's visual states
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        // Create drawable objects from the loaded textures
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        // Define the style for the ImageButton
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        // Create the ImageButton and set its size and position
        ImageButton button = new ImageButton(style);
        button.setSize(width, height);
        button.setPosition(x, y);

        // Add an input listener to handle interactions
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Reset cursor to default arrow
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                // Play button click sound
                game.soundManager.play("buttonClick");

                // Navigate to the previous screen by popping the current state and setting the new state
                game.screenManager.screenStateStack.pop();
                game.screenManager.setScreen(nextState);
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                // Change cursor to hand icon when hovering over the button
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                // Reset cursor to default arrow when exiting the button area
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        // Return the configured ImageButton
        return button;
    }

    /**
     * Creates an ImageTextButton with text and background textures.
     *
     * @param game         The SpaceInvadersGame instance to access shared resources and managers.
     * @param buttonText   The text displayed on the button.
     * @param upImagePath  The file path of the texture used when the button is not pressed.
     * @param downImagePath The file path of the texture used when the button is pressed.
     * @param width        The width of the button.
     * @param height       The height of the button.
     * @param x            The x-coordinate for positioning the button.
     * @param y            The y-coordinate for positioning the button.
     * @return             An ImageTextButton instance configured with the given properties.
     */
    public static ImageTextButton createButton(SpaceInvadersGame game, String buttonText, String upImagePath, String downImagePath, float width, float height, float x, float y) {
        // Load textures for the button's visual states
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        // Create drawable objects from the loaded textures
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        // Define the style for the ImageTextButton
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        // Load the font from the asset manager and set the font properties
        BitmapFont font = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);
        style.font = font;
        style.fontColor = Color.valueOf("4b692f");

        // Create the ImageTextButton and set its size and position
        ImageTextButton button = new ImageTextButton(buttonText, style);
        button.setSize(width, height);
        button.setPosition(x, y);

        // Add an input listener to handle hover interactions
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                // Change cursor to hand icon when hovering over the button
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                // Reset cursor to default arrow when exiting the button area
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        // Return the configured ImageTextButton
        return button;
    }

    /**
     * Creates an ImageTextButton with text and background textures.
     *
     * @param game         The SpaceInvadersGame instance to access shared resources and managers.
     * @param buttonText   The text displayed on the button.
     * @param upImagePath  The file path of the texture used when the button is not pressed.
     * @param downImagePath The file path of the texture used when the button is pressed.
     * @return             An ImageTextButton instance configured with the given properties without a set position and size.
     */
    public static ImageTextButton createButton(SpaceInvadersGame game, String buttonText, String upImagePath, String downImagePath) {
        // Load textures for the button's visual states
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        // Create drawable objects from the loaded textures
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        // Define the style for the ImageTextButton
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        // Load the font from the asset manager and set the font properties
        BitmapFont font = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);
        style.font = font;
        style.fontColor = Color.valueOf("4b692f");

        // Create the ImageTextButton and set its size and position
        ImageTextButton button = new ImageTextButton(buttonText, style);

        // Add an input listener to handle hover interactions
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                // Change cursor to hand icon when hovering over the button
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                // Reset cursor to default arrow when exiting the button area
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        // Return the configured ImageTextButton
        return button;
    }

    /**
     * Creates an ImageButton with background textures.
     *
     * @param game         The SpaceInvadersGame instance to access shared resources and managers.
     * @param upImagePath  The file path of the texture used when the button is not pressed.
     * @param downImagePath The file path of the texture used when the button is pressed.
     * @param width        The width of the button.
     * @param height       The height of the button.
     * @param x            The x-coordinate for positioning the button.
     * @param y            The y-coordinate for positioning the button.
     * @return             An ImageButton instance configured with the given properties.
     */
    public static ImageButton createImageButton(SpaceInvadersGame game, String upImagePath, String downImagePath, float width, float height, float x, float y) {
        // Load textures for the button's visual states
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        // Create drawable objects from the loaded textures
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        // Define the style for the ImageButton
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        // Create the ImageButton and set its size and position
        ImageButton button = new ImageButton(style);
        button.setSize(width, height);
        button.setPosition(x, y);

        // Add an input listener to handle hover interactions
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                // Change cursor to hand icon when hovering over the button
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                // Reset cursor to default arrow when exiting the button area
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        // Return the configured ImageButton
        return button;
    }
}
