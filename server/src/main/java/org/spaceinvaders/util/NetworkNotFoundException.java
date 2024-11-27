package org.spaceinvaders.util;

/**
 * NetworkNotFoundException.java
 * <br>
 * The NetworkNotFoundException exception is used when the server cannot connect
 * to the internet and send packets.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/13/2024
 */
public class NetworkNotFoundException extends RuntimeException
{
    /**
     * Creates a new NetworkNotFound Exception with the given message
     * @param message       the message to display
     */
    public NetworkNotFoundException(String message)
    {
        super(message);
    }
}
