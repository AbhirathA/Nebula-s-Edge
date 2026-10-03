package org.spaceinvaders.accounts;

import org.spaceinvaders.util.HTTPCode;

/**
 * AccountException.java
 * <br>
 * Thrown when an account operation fails. Carries the {@link HTTPCode} that the
 * HTTP handlers send back so the client can show a specific error message.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 2.0
 * @since 11/13/2024
 */
public class AccountException extends Exception {

    private final HTTPCode code;

    /**
     * Creates the exception.
     * @param code    the HTTP status the client should receive
     * @param message a human-readable description
     */
    public AccountException(HTTPCode code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Returns the HTTP status associated with this failure.
     * @return the HTTP status code
     */
    public HTTPCode getCode() {
        return code;
    }
}
