package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Screen;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.screens.*;
import com.spaceinvaders.frontend.utils.Command;
import com.spaceinvaders.util.InvalidScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ScreenManager {
    private static ScreenManager instance = null;
    private final SpaceInvadersGame game;
    private final Map<ScreenState, Screen> screens = new HashMap<>();
    private Screen currentScreen;
    private final StarsBackground starsBackground;
    private final PlanetsBackground planetsBackground;

    public final Stack<ScreenState> screenStateStack;

    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;

    private final float STAGE_WIDTH = WORLD_WIDTH * 2; // 360
    private final float STAGE_HEIGHT = WORLD_HEIGHT * 2; // 202.5

    public ScreenManager(SpaceInvadersGame game) {
        this.game = game;
        this.currentScreen = null;
        this.starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 50);
        this.planetsBackground = new PlanetsBackground(game.assetManager);
        screenStateStack = new Stack<>();
    }

    public ScreenState getRecentScreen() {
        if (screenStateStack.empty()) return ScreenState.LOGIN;

        ScreenState currentScreen = screenStateStack.pop();
        ScreenState recentScreen = screenStateStack.peek();
        screenStateStack.add(currentScreen);
        return recentScreen;
    }

    public static ScreenManager getInstance(SpaceInvadersGame game) {
        if(ScreenManager.instance == null) {
            ScreenManager.instance = new ScreenManager(game);
        }

        return ScreenManager.instance;
    }

    public void setScreen(ScreenState screenState) {
        if (screenState != ScreenState.LOADING)
            screenStateStack.add(screenState);

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

    public void setScreen(ScreenState screenState, Command command) throws InvalidScreen {
        if (screenState != ScreenState.LOADING) throw new InvalidScreen("Cannot add command to a screen which is not of type loading");

        if (this.currentScreen != null) {
            this.currentScreen.hide();
        }

        if (!this.screens.containsKey(ScreenState.LOADING)) {
            this.screens.put(ScreenState.LOADING, new LoadingScreen(this.game, command));
        } else {
            ((LoadingScreen)this.screens.get(ScreenState.LOADING)).setCommand(command);
        }

        this.game.setScreen(this.screens.get(ScreenState.LOADING));
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
            case LOGIN_GATEWAY:
                return new LoginGatewayScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case PAUSE:
                return new PauseScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground);
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
