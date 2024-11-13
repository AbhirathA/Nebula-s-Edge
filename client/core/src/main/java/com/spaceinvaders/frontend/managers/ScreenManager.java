package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Screen;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.screens.*;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {
    private static ScreenManager instance = null;
    private final SpaceInvadersGame game;
    private final Map<ScreenState, Screen> screens = new HashMap<>();
    private Screen currentScreen;

    private ScreenManager(SpaceInvadersGame game) {
        this.game = game;
        this.currentScreen = null;
    }

    public static ScreenManager getInstance(SpaceInvadersGame game) {
        if(ScreenManager.instance == null) {
            ScreenManager.instance = new ScreenManager(game);
        }

        return ScreenManager.instance;
    }

    public void setScreen(ScreenState screenState) {
        if (this.currentScreen != null) {
            this.currentScreen.hide();
        }

        if (!this.screens.containsKey(screenState)) {
            this.screens.put(screenState, createScreen(screenState));
        }

        this.currentScreen = this.screens.get(screenState);
        this.game.setScreen(this.currentScreen);
        this.currentScreen.show();
    }

    private Screen createScreen(ScreenState screenState) {
        switch (screenState) {
            case LOADING:
                return new LoadingScreen(this.game);
            case LOGIN:
                return new LoginScreen(this.game);
            case SIGNUP:
                return new SignupScreen(this.game);
            case MAIN_MENU:
                return new MainMenuScreen(this.game);
            default:
                throw new IllegalArgumentException("Unknown screen state: " + screenState);
        }
    }

    public Screen getCurrentScreen() {
        return this.currentScreen;
    }

    public void dispose() {
        for (Screen screen : this.screens.values()) {
            screen.dispose();
        }
    }
}
