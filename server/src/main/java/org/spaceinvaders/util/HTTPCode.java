package org.spaceinvaders.util;

/**
 * HttpCode.java
 * <br>
 * The HttpCode is a list of common HTTP Codes used by the server.
 *
 * @author Gathik
 * @author Aryan
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public enum HTTPCode
{
    /** Request succeeded without issues (200). */
    SUCCESS(200),

    /** Request contains invalid JSON syntax (400). */
    INVALID_JSON(400),

    /** Input data is invalid (401). */
    INVALID_INPUT(401),

    /** Email address already exists in the system (402). */
    EMAIL_EXISTS(402),

    /** Password does not meet strength requirements (403). */
    WEAK_PASSWORD(403),

    /** Email address was not found in the system (404). */
    EMAIL_NOT_FOUND(404),

    /** User is not authorized to perform this action (405). */
    UNAUTHORIZED(405),

    /** Too many requests were made in a short period (429). */
    TOO_MANY_ATTEMPTS(429),

    /** Cannot establish a connection to the server (500). */
    CANNOT_CONNECT(500),

    /** A database error occurred (501). */
    DATABASE_ERROR(501),

    /** A generic server error occurred (502). */
    SERVER_ERROR(502),

    /** An unspecified error occurred (503). */
    ERROR(503);

    /**
     * The response code.
     */
    private final int code;

    /**
     * Creates a new HTTP Code
     *
     * @param code the HTTP code
     */
    HTTPCode(int code)
    {
        this.code = code;
    }

    /**
     * Returns the HTTP code of this enum.
     *
     * @return the HTTP code
     */
    public int getCode()
    {
        return this.code;
    }
}