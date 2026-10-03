package com.spaceinvaders.web;

import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import com.spaceinvaders.frontend.SpaceInvadersGame;

/**
 * WebLauncher.java
 * <br>
 * Entry point of the browser build (compiled to JavaScript by TeaVM).
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 10/03/2026
 */
public class WebLauncher {
    public static void main(String[] args) {
        WebApplicationConfiguration config = new WebApplicationConfiguration();
        // 0 x 0 lets the canvas fill the page; the game's FitViewports letterbox to 16:9
        config.width = 0;
        config.height = 0;
        new WebApplication(new SpaceInvadersGame(new LocalBackend()), config);
    }
}
