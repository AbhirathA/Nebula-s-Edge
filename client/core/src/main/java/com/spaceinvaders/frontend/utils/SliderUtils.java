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

public class SliderUtils {

    public static Slider createSlider(MyAssetManager assetManager, float width, float height, float x, float y) {
        // load textures for slider and knob from asset manager
        Texture sliderBackgroundTexture = assetManager.get("textures/sliderBackground.png", Texture.class);
        Texture sliderKnobTexture = assetManager.get("textures/sliderKnob.png", Texture.class);

        // wrap the textures in TextureRegionDrawable making them drawable
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTexture));
        TextureRegionDrawable sliderKnob = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));

        // wrap the Sprite in SpriteDrawable making them drawable
        SpriteDrawable sliderKnobBefore = new SpriteDrawable(new Sprite(sliderBackgroundTexture));
        SpriteDrawable sliderKnobAfter = new SpriteDrawable(new Sprite(sliderBackgroundTexture));

        sliderKnobBefore.getSprite().setColor(new Color(1.2f, 1.2f, 1.2f, 1f)); // Lighter
        sliderKnobAfter.getSprite().setColor(new Color(1f, 0.65f, 0.2f, 1f)); // Darker

        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = sliderBackground;
        style.knob = sliderKnob;
        style.knobBefore = sliderKnobBefore;
        style.knobAfter = sliderKnobAfter;

        Slider slider = new Slider(0, 100, 1, false, style);
        slider.setSize(width, height);
        slider.setPosition(x, y);

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

    public static Slider createSlider(MyAssetManager assetManager) {
        // load textures for slider and knob from asset manager
        Texture sliderBackgroundTexture = assetManager.get("textures/sliderBackground.png", Texture.class);
        Texture sliderKnobTexture = assetManager.get("textures/sliderKnob.png", Texture.class);

        // wrap the textures in TextureRegionDrawable making them drawable
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTexture));
        TextureRegionDrawable sliderKnob = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));

        // wrap the Sprite in SpriteDrawable making them drawable
        SpriteDrawable sliderKnobBefore = new SpriteDrawable(new Sprite(sliderBackgroundTexture));
        SpriteDrawable sliderKnobAfter = new SpriteDrawable(new Sprite(sliderBackgroundTexture));

        sliderKnobBefore.getSprite().setColor(new Color(1.2f, 1.2f, 1.2f, 1f)); // Lighter
        sliderKnobAfter.getSprite().setColor(new Color(1f, 0.65f, 0.2f, 1f)); // Darker

        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = sliderBackground;
        style.knob = sliderKnob;
        style.knobBefore = sliderKnobBefore;
        style.knobAfter = sliderKnobAfter;

        Slider slider = new Slider(0, 100, 1, false, style);

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

    public static Label getValueLabel(MyAssetManager assetManager, Slider slider, float width, float height, float x, float y) {
        Label label = LabelUtils.createLabel("", assetManager.get("fonts/minecraft.fnt", BitmapFont.class),
                x + width + 5, y + height / 2);
        slider.addListener(event -> {
            label.setText((int) slider.getValue());
            return false;
        });

        return label;
    }

    public static Label getValueLabel(MyAssetManager assetManager, Slider slider) {
        Label label = LabelUtils.createLabel("", assetManager.get("fonts/minecraft.fnt", BitmapFont.class));
        slider.addListener(event -> {
            label.setText((int) slider.getValue());
            return false;
        });

        return label;
    }
}
