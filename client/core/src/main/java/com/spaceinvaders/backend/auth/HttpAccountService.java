package com.spaceinvaders.backend.auth;

import com.spaceinvaders.backend.auth.utils.AuthenticationException;

/**
 * HttpAccountService.java
 * <br>
 * Accounts kept by the game server, reached through {@link AuthenticationManager}.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public class HttpAccountService implements AccountService {

    @Override
    public String signIn(String email, String password) throws AuthenticationException {
        return AuthenticationManager.signIn(email, password);
    }

    @Override
    public void signUp(String email, String password, String confirmPassword) throws AuthenticationException {
        AuthenticationManager.signUp(email, password, confirmPassword);
    }

    @Override
    public String getUserData(String token) throws AuthenticationException {
        return AuthenticationManager.getUserData(token);
    }

    @Override
    public void resetPassword(String email) throws AuthenticationException {
        AuthenticationManager.resetPassword(email);
    }
}
