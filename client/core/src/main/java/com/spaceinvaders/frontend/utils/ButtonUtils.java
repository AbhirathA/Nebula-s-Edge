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

public class ButtonUtils {
    public static ImageTextButton createScreenNavigationButton(SpaceInvadersGame game, String buttonText, String upImagePath, String downImagePath, float width, float height, float x, float y, ScreenState targetScreen) {
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        BitmapFont font = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);
        style.font = font;
        style.fontColor = Color.valueOf("4b692f");

        ImageTextButton button = new ImageTextButton(buttonText, style);
        button.setSize(width, height);
        button.setPosition(x, y);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                if(targetScreen == null){
                    System.out.println("Move to new screen");
                }
                else {
                    game.screenManager.setScreen(targetScreen);
                }
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return button;
    }

    public static ImageButton createBackButton(SpaceInvadersGame game, String upImagePath, String downImagePath, float width, float height, float x, float y, ScreenState nextState) {
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        ImageButton button = new ImageButton(style);
        button.setSize(width, height);
        button.setPosition(x, y);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

                game.screenManager.screenStateStack.pop();
                game.screenManager.setScreen(nextState);
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return button;
    }

    public static ImageTextButton createButton(SpaceInvadersGame game, String buttonText, String upImagePath, String downImagePath, float width, float height, float x, float y) {
        Texture upTexture = game.assetManager.get(upImagePath, Texture.class);
        Texture downTexture = game.assetManager.get(downImagePath, Texture.class);

        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        TextureRegionDrawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = upDrawable;
        style.down = downDrawable;

        BitmapFont font = game.assetManager.get("fonts/minecraft.fnt", BitmapFont.class);
        style.font = font;  // Set the BitmapFont for the button text
        style.fontColor = Color.valueOf("4b692f");

        ImageTextButton button = new ImageTextButton(buttonText, style);
        button.setSize(width, height);
        button.setPosition(x, y);

        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        return button;
    }
}
