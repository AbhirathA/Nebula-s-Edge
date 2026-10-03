package com.spaceinvaders.backend.auth;

import com.spaceinvaders.backend.auth.utils.AuthenticationException;

/**
 * AccountService.java
 * <br>
 * Sign up, sign in and profile lookup, as used by the login screens.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public interface AccountService {

    /**
     * Checks the credentials.
     * @return a session token for {@link #getUserData(String)}
     * @throws AuthenticationException with a message for the player if sign-in fails
     */
    String signIn(String email, String password) throws AuthenticationException;

    /**
     * Creates an account.
     * @throws AuthenticationException with a message for the player if the account can't be created
     */
    void signUp(String email, String password, String confirmPassword) throws AuthenticationException;

    /**
     * Returns the signed-in player's kill count.
     * @throws AuthenticationException if the session is not valid
     */
    String getUserData(String token) throws AuthenticationException;

    /**
     * Starts a password reset.
     * @throws AuthenticationException with a message for the player if it isn't possible
     */
    void resetPassword(String email) throws AuthenticationException;
}
