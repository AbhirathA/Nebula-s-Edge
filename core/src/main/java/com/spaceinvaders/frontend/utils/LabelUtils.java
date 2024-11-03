package com.spaceinvaders.frontend.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelUtils {
    public static Label createLabel(String text, float x, float y) {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/minecraft.fnt"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        Label label = new Label(text, labelStyle);
        label.setPosition(x, y);

        return label;
    }
}
