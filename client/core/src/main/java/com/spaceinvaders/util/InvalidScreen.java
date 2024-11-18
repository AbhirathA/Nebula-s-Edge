package com.spaceinvaders.util;

/**
 * Exception thrown to indicate an invalid screen state error occurred during the set-screen process.
 * This exception extends {@link Exception}, making it a checked exception that must be
 * either caught or declared in the method signature.
 */
public class InvalidScreen extends Exception
{
    /**
     * Constructs a new {@code InavlidScreen} with the specified detail message.
     * The message provides additional information about the cause of the InvalidScreen.
     *
     * @param message The detail message explaining the reason for the invalidScreen exception.
     */
    public InvalidScreen(String message)
    {
        super(message);
    }
}
