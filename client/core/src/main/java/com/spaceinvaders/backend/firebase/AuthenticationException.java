package com.spaceinvaders.backend.firebase;

/**
 * Exception thrown to indicate an authentication error occurred during the sign-in process.
 * This exception extends {@link Exception}, making it a checked exception that must be
 * either caught or declared in the method signature.
 * It is typically used to signal that user authentication failed due to invalid credentials
 * or other issues encountered with the authentication process.
 */
public class AuthenticationException extends Exception
{
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
