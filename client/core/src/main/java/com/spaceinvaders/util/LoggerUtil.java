package com.spaceinvaders.util;

import com.badlogic.gdx.Gdx;

/**
 * LoggerUtil.java
 * <br>
 * The LoggerUtil class represents a basic utility class that handles all the client
 * logging that is required. It writes through libGDX's logger, which prints to the
 * console on desktop and to the browser console in the web build.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/15/2024
 */
public class LoggerUtil
{
    private static final String TAG = "NebulasEdge";

    private LoggerUtil() {}

    public static void logInfo(String message)
    {
        if (Gdx.app != null) Gdx.app.log(TAG, message);
        else System.out.println(TAG + ": " + message);
    }

    public static void logError(String message)
    {
        if (Gdx.app != null) Gdx.app.error(TAG, message);
        else System.err.println(TAG + ": " + message);
    }

    public static void logException(String message, Exception e)
    {
        if (Gdx.app != null) Gdx.app.error(TAG, message, e);
        else {
            System.err.println(TAG + ": " + message);
            e.printStackTrace();
        }
    }
}
