package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Screen;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.screens.*;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {
    private static ScreenManager instance;
    private final SpaceInvadersGame game;
    private final Map<ScreenState, Screen> screens = new HashMap<>();
    private Screen currentScreen;

    private ScreenManager(SpaceInvadersGame game) {
        this.game = game;
    }

    public static ScreenManager getInstance(SpaceInvadersGame game) {
        if(instance == null) {
            instance = new ScreenManager(game);
        }

        return instance;
    }

    public void setScreen(ScreenState screenState) {
        if (currentScreen != null) {
            currentScreen.hide();
        }

        if (!screens.containsKey(screenState)) {
            screens.put(screenState, createScreen(screenState));
        }

        currentScreen = screens.get(screenState);
        game.setScreen(currentScreen);
        currentScreen.show();
    }

    private Screen createScreen(ScreenState screenState) {
        switch (screenState) {
            case LOADING:
                return new LoadingScreen(game);
            case LOGIN:
                return new LoginScreen(game);
            case SIGNUP:
                return new SignupScreen(game);
            case MAIN_MENU:
                return new MainMenuScreen(game);
            default:
                throw new IllegalArgumentException("Unknown screen state: " + screenState);
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void dispose() {
        for (Screen screen : screens.values()) {
            screen.dispose();
        }
    }
}
