package com.spaceinvaders.backend.firebase.utils;

/**
 * <p>
 * The <code>HTTPCode</code> enum defines a list of common HTTP status codes
 * used by the server to indicate the outcome of operations.
 * </p>
 *
 * <p>
 * This enum associates each status code with its respective integer value
 * and provides utility methods for retrieving codes and their corresponding enums.
 * </p>
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
public enum HTTPCode {
    /**
     * Indicates the operation was successful.
     */
    SUCCESS(200),

    /**
     * Indicates that the provided JSON data is invalid.
     */
    INVALID_JSON(400),

    /**
     * Indicates that the input provided is invalid.
     */
    INVALID_INPUT(401),

    /**
     * Indicates that the email address already exists.
     */
    EMAIL_EXISTS(402),

    /**
     * Indicates that the provided password is too weak.
     */
    WEAK_PASSWORD(403),

    /**
     * Indicates that the requested email address was not found.
     */
    EMAIL_NOT_FOUND(404),

    /**
     * Indicates that the request is unauthorized.
     */
    UNAUTHORIZED(405),

    /**
     * Indicates too many attempts were made, typically due to rate-limiting.
     */
    TOO_MANY_ATTEMPTS(429),

    /**
     * Indicates that the server cannot connect to a required resource or service.
     */
    CANNOT_CONNECT(500),

    /**
     * Indicates a database-related error occurred.
     */
    DATABASE_ERROR(501),

    /**
     * Indicates a general server error occurred.
     */
    SERVER_ERROR(502),

    /**
     * Indicates an unspecified error occurred.
     */
    ERROR(503);

    private final int code;

    HTTPCode(int code) {
        this.code = code;
    }

    /**
     * Returns the integer value of this HTTP code.
     *
     * @return the integer value of this HTTP code.
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns the corresponding <code>HTTPCode</code> enum for the given integer code.
     *
     * @param code the integer HTTP code to match.
     * @return the matching <code>HTTPCode</code> enum.
     * @throws IllegalArgumentException if the provided code does not match any enum constant.
     */
    public static HTTPCode fromCode(int code) {
        for (HTTPCode httpCode : HTTPCode.values()) {
            if (httpCode.getCode() == code) {
                return httpCode;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP code: " + code);
    }
}
