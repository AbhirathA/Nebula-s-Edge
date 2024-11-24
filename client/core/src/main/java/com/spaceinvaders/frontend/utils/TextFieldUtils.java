package com.spaceinvaders.frontend.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.spaceinvaders.frontend.managers.MyAssetManager;

public class TextFieldUtils {

    /**
     * Creates a standard TextField with the specified parameters.
     *
     * @param initialText   The initial text displayed in the TextField.
     * @param assetManager  The asset manager used to load fonts and textures.
     * @param width         The width of the TextField.
     * @param height        The height of the TextField.
     * @param x             The x-coordinate position of the TextField.
     * @param y             The y-coordinate position of the TextField.
     * @return              A configured TextField instance.
     */
    public static TextField createTextField(String initialText, MyAssetManager assetManager, float width, float height, float x, float y) {
        // Load the font from the asset manager
        BitmapFont font = assetManager.get("fonts/minecraft.fnt", BitmapFont.class);

        // Load textures for background, cursor, and selection from the asset manager
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/outline.png", Texture.class)));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/cursor.png", Texture.class)));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/selection.png", Texture.class)));

        // Set up the TextField style with loaded assets
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.background = backgroundDrawable;
        style.cursor = cursorDrawable;
        style.selection = selectionDrawable;

        // Create a new TextField with the specified style and initial text
        TextField textField = new TextField(initialText, style);
        textField.setSize(width, height);
        textField.setPosition(x, y);
        textField.setAlignment(Align.center);

        // Add a listener to change the cursor appearance when hovering over the TextField
        textField.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Ibeam); // I-beam cursor when hovering
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow); // Arrow cursor when exiting
            }
        });

        return textField; // Return the configured TextField
    }

    /**
     * Creates a password-protected TextField with masking characters.
     *
     * @param assetManager  The asset manager used to load fonts and textures.
     * @param width         The width of the TextField.
     * @param height        The height of the TextField.
     * @param x             The x-coordinate position of the TextField.
     * @param y             The y-coordinate position of the TextField.
     * @return              A configured password-protected TextField instance.
     */
    public static TextField createPasswordField(MyAssetManager assetManager, float width, float height, float x, float y) {
        // Load the font from the asset manager
        BitmapFont font = assetManager.get("fonts/minecraft.fnt", BitmapFont.class);

        // Load textures for background, cursor, and selection from the asset manager
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/outline.png", Texture.class)));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/cursor.png", Texture.class)));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/selection.png", Texture.class)));

        // Set up the TextField style with loaded assets
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.background = backgroundDrawable;
        style.cursor = cursorDrawable;
        style.selection = selectionDrawable;

        // Create a new TextField with the specified style
        TextField textField = new TextField("", style);
        textField.setSize(width, height);
        textField.setPosition(x, y);
        textField.setAlignment(Align.center);
        textField.setPasswordMode(true);
        textField.setPasswordCharacter('*');

        // Add a listener to change the cursor appearance when hovering over the TextField
        textField.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Ibeam); // I-beam cursor when hovering
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow); // Arrow cursor when exiting
            }
        });

        return textField; // Return the configured password TextField
    }
}
