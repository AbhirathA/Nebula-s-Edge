package com.spaceinvaders.frontend.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class TextFieldUtils {

    public static TextField createTextField(String initialText, float width, float height, float x, float y) {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/minecraft.fnt"));

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/outline.png"))));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/cursor.png"))));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/selection.png"))));

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

//        textField.setTextFieldFilter(new TextField.TextFieldFilter() {
//            @Override
//            public boolean acceptChar(TextField textField, char c) {
//                return textField.getText().length() < 15; // Limit to maxChars characters
//            }
//        });

        return textField;
    }

    public static TextField createPasswordField(float width, float height, float x, float y) {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/minecraft.fnt"));

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/outline.png"))));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/cursor.png"))));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/selection.png"))));

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

//        textField.setTextFieldFilter(new TextField.TextFieldFilter() {
//            @Override
//            public boolean acceptChar(TextField textField, char c) {
//                return textField.getText().length() < 15; // Limit to maxChars characters
//            }
//        });

        return textField;
    }
}
