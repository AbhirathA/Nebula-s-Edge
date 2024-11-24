package com.spaceinvaders.frontend.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spaceinvaders.frontend.managers.MyAssetManager;
import com.badlogic.gdx.graphics.Color;

/**
 * Utility class for creating and customizing sliders in a LibGDX application.
 * Includes methods for creating sliders with various configurations and attaching value labels.
 */
public class SliderUtils {

    /**
     * Creates a slider with a custom style and specified position and size.
     *
     * @param assetManager The asset manager to retrieve textures and resources.
     * @param width        The width of the slider.
     * @param height       The height of the slider.
     * @param x            The x-coordinate position of the slider.
     * @param y            The y-coordinate position of the slider.
     * @return A new Slider instance with the specified style and position.
     */
    public static Slider createSlider(MyAssetManager assetManager, float width, float height, float x, float y) {
        // Load textures for slider background and knob from the asset manager.
        Texture sliderBackgroundTexture = assetManager.get("textures/sliderBackground.png", Texture.class);
        Texture sliderKnobTexture = assetManager.get("textures/sliderKnob.png", Texture.class);

        // Wrap textures in TextureRegionDrawable to make them drawable.
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTexture));
        TextureRegionDrawable sliderKnob = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));

        // Create drawables for the slider's knob states (before and after).
        SpriteDrawable sliderKnobBefore = new SpriteDrawable(new Sprite(sliderBackgroundTexture));
        SpriteDrawable sliderKnobAfter = new SpriteDrawable(new Sprite(sliderBackgroundTexture));

        // Customize the colors of the knob for before and after states.
        sliderKnobBefore.getSprite().setColor(new Color(1.2f, 1.2f, 1.2f, 1f)); // Lighter color.
        sliderKnobAfter.getSprite().setColor(new Color(1f, 0.65f, 0.2f, 1f));   // Darker color.

        // Create and configure the slider style.
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = sliderBackground;
        style.knob = sliderKnob;
        style.knobBefore = sliderKnobBefore;
        style.knobAfter = sliderKnobAfter;

        // Create the slider with the specified style.
        Slider slider = new Slider(0, 100, 1, false, style);
        slider.setSize(width, height);
        slider.setPosition(x, y);

        // Add an input listener to change the cursor to a hand when hovering over the slider.
        slider.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return slider;
    }

    /**
     * Creates a slider with a custom style, without specifying position and size.
     *
     * @param assetManager The asset manager to retrieve textures and resources.
     * @return A new Slider instance with the specified style.
     */
    public static Slider createSlider(MyAssetManager assetManager) {
        // Load textures for slider background and knob from the asset manager.
        Texture sliderBackgroundTexture = assetManager.get("textures/sliderBackground.png", Texture.class);
        Texture sliderKnobTexture = assetManager.get("textures/sliderKnob.png", Texture.class);

        // Wrap textures in TextureRegionDrawable to make them drawable.
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTexture));
        TextureRegionDrawable sliderKnob = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));

        // Create drawables for the slider's knob states (before and after).
        SpriteDrawable sliderKnobBefore = new SpriteDrawable(new Sprite(sliderBackgroundTexture));
        SpriteDrawable sliderKnobAfter = new SpriteDrawable(new Sprite(sliderBackgroundTexture));

        // Customize the colors of the knob for before and after states.
        sliderKnobBefore.getSprite().setColor(new Color(1.2f, 1.2f, 1.2f, 1f)); // Lighter color.
        sliderKnobAfter.getSprite().setColor(new Color(1f, 0.65f, 0.2f, 1f));   // Darker color.

        // Create and configure the slider style.
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = sliderBackground;
        style.knob = sliderKnob;
        style.knobBefore = sliderKnobBefore;
        style.knobAfter = sliderKnobAfter;

        // Create the slider with the specified style.
        Slider slider = new Slider(0, 100, 1, false, style);

        // Add an input listener to change the cursor to a hand when hovering over the slider.
        slider.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return slider;
    }

    /**
     * Creates a label to display the current value of a slider at a specified position.
     *
     * @param assetManager The asset manager to retrieve fonts and resources.
     * @param slider       The slider whose value the label will display.
     * @param width        The width of the slider.
     * @param height       The height of the slider.
     * @param x            The x-coordinate position of the label.
     * @param y            The y-coordinate position of the label.
     * @return A Label instance displaying the slider's value.
     */
    public static Label getValueLabel(MyAssetManager assetManager, Slider slider, float width, float height, float x, float y) {
        // Create a label near the slider with the specified font.
        Label label = LabelUtils.createLabel("", assetManager.get("fonts/minecraft.fnt", BitmapFont.class),
            x + width + 5, y + height / 2);

        // Add a listener to update the label text whenever the slider's value changes.
        slider.addListener(event -> {
            label.setText((int) slider.getValue());
            return false;
        });

        return label;
    }

    /**
     * Creates a label to display the current value of a slider without specifying position.
     *
     * @param assetManager The asset manager to retrieve fonts and resources.
     * @param slider       The slider whose value the label will display.
     * @return A Label instance displaying the slider's value.
     */
    public static Label getValueLabel(MyAssetManager assetManager, Slider slider) {
        // Create a label with the specified font.
        Label label = LabelUtils.createLabel("", assetManager.get("fonts/minecraft.fnt", BitmapFont.class));

        // Add a listener to update the label text whenever the slider's value changes.
        slider.addListener(event -> {
            label.setText((int) slider.getValue());
            return false;
        });

        return label;
    }
}
