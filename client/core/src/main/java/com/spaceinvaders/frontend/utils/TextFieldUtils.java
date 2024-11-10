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

    public static TextField createTextField(String initialText, MyAssetManager assetManager, float width, float height, float x, float y) {
        BitmapFont font = assetManager.get("fonts/minecraft.fnt", BitmapFont.class);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/outline.png", Texture.class)));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/cursor.png", Texture.class)));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/selection.png", Texture.class)));

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.background = backgroundDrawable;
        style.cursor = cursorDrawable;
        style.selection = selectionDrawable;

        TextField textField = new TextField(initialText, style);
        textField.setSize(width, height);
        textField.setPosition(x, y);
        textField.setAlignment(Align.center);

        textField.addListener( new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Ibeam);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return textField;
    }

    public static TextField createPasswordField(MyAssetManager assetManager, float width, float height, float x, float y) {
        BitmapFont font = assetManager.get("fonts/minecraft.fnt", BitmapFont.class);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/outline.png", Texture.class)));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/cursor.png", Texture.class)));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/selection.png", Texture.class)));

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.background = backgroundDrawable;
        style.cursor = cursorDrawable;
        style.selection = selectionDrawable;

        TextField textField = new TextField("", style);
        textField.setSize(width, height);
        textField.setPosition(x, y);
        textField.setAlignment(Align.center);
        textField.setPasswordMode(true);
        textField.setPasswordCharacter('*');

        textField.addListener( new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Ibeam);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return textField;
    }
}
