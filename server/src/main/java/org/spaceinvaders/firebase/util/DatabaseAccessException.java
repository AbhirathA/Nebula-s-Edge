/**
 * DatabaseAccessException.java
 * The DatabaseAccessException exception is used when the server cannot connect
 * to the database.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/13/2024
 */

package org.spaceinvaders.firebase.util;

public class DatabaseAccessException extends Exception
{
    /**
     * @param message   the message to display when this exception is thrown
     */
    public DatabaseAccessException(String message)
    {
        super(message);
    }
}
