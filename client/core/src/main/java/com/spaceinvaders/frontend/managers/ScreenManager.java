package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Screen;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.screens.*;
import com.spaceinvaders.frontend.utils.Command;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {
    private static ScreenManager instance = null;
    private final SpaceInvadersGame game;
    private final Map<ScreenState, Screen> screens = new HashMap<>();
    private Screen currentScreen;
    private StarsBackground starsBackground;
    private PlanetsBackground planetsBackground;

    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;

    private final float STAGE_WIDTH = WORLD_WIDTH * 3/2;
    private final float STAGE_HEIGHT = WORLD_HEIGHT * 3/2;

    public ScreenManager(SpaceInvadersGame game) {
        this.game = game;
        this.currentScreen = null;
        this.starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 30);
        this.planetsBackground = new PlanetsBackground(game.assetManager);
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
            this.screens.put(screenState, this.createScreen(screenState));
        }

        this.currentScreen = this.screens.get(screenState);
        this.game.setScreen(this.currentScreen);
        this.currentScreen.show();
    }

    private static class CommandClass implements Command {
        @Override
        public void execute() {}

        @Override
        public void onUpdate() {}

        @Override
        public boolean update() { return true; }
    }

    private Screen createScreen(ScreenState screenState) {
        switch (screenState) {
            case LOADING:
                return new LoadingScreen(this.game, new CommandClass());
            case LOGIN:
                return new LoginScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case SIGNUP:
                return new SignupScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case MAIN_MENU:
                return new MainMenuScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
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
