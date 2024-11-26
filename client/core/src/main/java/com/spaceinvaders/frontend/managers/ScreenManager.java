package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.PlanetsBackground;
import com.spaceinvaders.frontend.background.StarsBackground;
import com.spaceinvaders.frontend.screens.*;
import com.spaceinvaders.frontend.utils.Command;
import com.spaceinvaders.frontend.utils.InvalidScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ScreenManager implements Disposable {
    // Singleton instance for ScreenManager
    private static ScreenManager instance = null;

    private final SpaceInvadersGame game; // Reference to the game object
    private final Map<ScreenState, Screen> screens = new HashMap<>(); // Map to store the different screens based on ScreenState
    private Screen currentScreen; // The current screen being displayed
    private final StarsBackground starsBackground; // Common Stars background for certain screens
    private final PlanetsBackground planetsBackground; // Common Planets background for certain screens

    public final Stack<ScreenState> screenStateStack; // Stack to maintain the order of screens for implementation of back button

    // Constants for the world and stage dimensions
    private final float WORLD_WIDTH = 240;
    private final float WORLD_HEIGHT = 135;
    private final float STAGE_WIDTH = WORLD_WIDTH * 2; // 480
    private final float STAGE_HEIGHT = WORLD_HEIGHT * 2; // 270

    // Private constructor for the ScreenManager
    public ScreenManager(SpaceInvadersGame game) {
        this.game = game;
        this.currentScreen = null;
        this.starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 50);
        this.planetsBackground = new PlanetsBackground(game.assetManager);
        this.screenStateStack = new Stack<>();
    }

    // Returns the most recently accessed screen for implementation of back button
    public ScreenState getRecentScreen() {
        if (screenStateStack.empty()) return ScreenState.LOGIN; // Default to LOGIN if the stack is empty

        ScreenState currentScreen = screenStateStack.pop(); // Remove the current screen from the stack
        ScreenState recentScreen = screenStateStack.peek(); // Peek the most recent screen
        screenStateStack.add(currentScreen); // Add the current screen back to the stack
        return recentScreen; // Return the recent screen state
    }

    // Singleton pattern to get the ScreenManager instance
    public static ScreenManager getInstance(SpaceInvadersGame game) {
        if(ScreenManager.instance == null) {
            ScreenManager.instance = new ScreenManager(game); // Create the instance if it doesn't exist
        }

        return ScreenManager.instance; // Return the singleton instance
    }

    // Set the screen based on the given screen state
    public void setScreen(ScreenState screenState) {
        if (screenState != ScreenState.LOADING)
            screenStateStack.add(screenState); // Add screen to stack unless it's LOADING screen

        // If the screen isn't already created, create and store it in the map
        if (!this.screens.containsKey(screenState)) {
            this.screens.put(screenState, this.createScreen(screenState));
        }

        this.currentScreen = this.screens.get(screenState); // Get the screen from the map
        this.game.setScreen(this.currentScreen); // Set the screen for the game
    }

    // Set the screen and execute a command if the screen state is LOADING
    public void setScreen(ScreenState screenState, Command command) throws InvalidScreen {
        if (screenState != ScreenState.LOADING) throw new InvalidScreen("Cannot add command to a screen which is not of type loading");

        if (this.currentScreen != null) {
            this.currentScreen.hide(); // Hide the current screen before switching
        }

        if (!this.screens.containsKey(ScreenState.LOADING)) {
            // Create the LOADING screen with the given command if it doesn't exist
            this.screens.put(ScreenState.LOADING, new LoadingScreen(this.game, command));
        } else {
            ((LoadingScreen)this.screens.get(ScreenState.LOADING)).setCommand(command); // Set the command if the screen already exists
        }

        this.game.setScreen(this.screens.get(ScreenState.LOADING)); // Set the LOADING screen
        this.currentScreen.show(); // Show the LOADING screen
    }

    // A dummy Command implementation (could be replaced with actual commands)
    private static class CommandClass implements Command {
        @Override
        public void execute() {}

        @Override
        public void onUpdate() {}

        @Override
        public boolean update() { return true; }
    }

    // Create a new screen based on the screen state
    // This is done so that all the screens maintain state
    private Screen createScreen(ScreenState screenState) {
        switch (screenState) {
            case LOADING:
                return new LoadingScreen(this.game, new CommandClass()); // Create and return a LoadingScreen
            case LOGIN:
                return new LoginScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case SIGNUP:
                return new SignupScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case MAIN_MENU:
                return new MainMenuScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case LOGIN_GATEWAY:
                return new LoginGatewayScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case SINGLEPLAYER_PAUSE:
                return new PauseScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, ScreenState.SINGLEPLAYER_GAMEPLAY);
            case MULTIPLAYER_PAUSE:
                return new PauseScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, ScreenState.MULTIPLAYER_GAMEPLAY);
            case SINGLEPLAYER_GAMEPLAY:
                return new GameplaySingleplayerScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, 720, 405);
            case MULTIPLAYER_GAMEPLAY:
                return new GameplayMultiplayerScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, 720, 405);
            case OPTIONS:
                return new OptionsScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            case RESET_PASSWORD:
                return new ResetPasswordScreen(this.game, WORLD_WIDTH, WORLD_HEIGHT, STAGE_WIDTH, STAGE_HEIGHT, starsBackground, planetsBackground);
            default:
                throw new IllegalArgumentException("Unknown screen state: " + screenState); // Throw an error if the screen state is unknown
        }
    }

    // Get the current screen being displayed
    public Screen getCurrentScreen() {
        return this.currentScreen;
    }

    // Dispose of all screens when the manager is disposed
    @Override
    public void dispose() {
        for (Screen screen : this.screens.values()) {
            screen.dispose(); // Dispose each screen
        }
    }
}
