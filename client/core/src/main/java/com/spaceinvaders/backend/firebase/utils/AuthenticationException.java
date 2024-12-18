package com.spaceinvaders.backend.firebase.utils;

/**
 * AuthenticationException.java
 * <br>
 * Exception thrown to indicate an authentication error occurred during the sign-in process.
 * This exception extends {@link Exception}, making it a checked exception that must be
 * either caught or declared in the method signature.
 * It is typically used to signal that user authentication failed due to invalid credentials
 * or other issues encountered with the authentication process.
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public class AuthenticationException extends Exception {
    /**
     * Constructs a new {@code AuthenticationException} with the specified detail message.
     * The message provides additional information about the cause of the authentication failure.
     *
     * @param message The detail message explaining the reason for the authentication failure.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
