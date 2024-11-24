package com.spaceinvaders.frontend.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Utility class for creating labels in a LibGDX application.
 * Provides methods to create and customize labels with different parameters.
 */
public class LabelUtils {

    /**
     * Creates a label with the specified text, font, and position.
     *
     * @param text The text to be displayed in the label.
     * @param font The font to be used for rendering the label's text.
     * @param x The x-coordinate position of the label.
     * @param y The y-coordinate position of the label.
     * @return A new Label instance with the specified text, font, and position.
     */
    public static Label createLabel(String text, BitmapFont font, float x, float y) {
        // Create a label style and set its font and font color.
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Create a label with the specified text and style.
        Label label = new Label(text, labelStyle);

        // Set the position of the label to the specified x and y coordinates.
        label.setPosition(x, y);

        return label; // Return the configured label.
    }

    /**
     * Creates a label with the specified text and font.
     * The position of the label is not set.
     *
     * @param text The text to be displayed in the label.
     * @param font The font to be used for rendering the label's text.
     * @return A new Label instance with the specified text and font.
     */
    public static Label createLabel(String text, BitmapFont font) {
        // Create a label style and set its font and font color.
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        // Create a label with the specified text and style.
        Label label = new Label(text, labelStyle);

        return label; // Return the configured label.
    }
}
