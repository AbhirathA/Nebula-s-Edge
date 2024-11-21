/**
 * HttpCode.java
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

package com.spaceinvaders.backend.firebase.utils;

public enum HTTPCode
{
    SUCCESS(200),
    INVALID_JSON(400),
    INVALID_ID_PASS(401),
    EMAIL_EXISTS(402),
    WEAK_PASSWORD(403),
    CANNOT_CONNECT(500),
    DATABASE_ERROR(501),
    SERVER_ERROR(502),
    ERROR(503);

    private final int code;

    private HTTPCode(int code)
    {
        this.code = code;
    }

    /**
     * Returns the code of this HTTPCode
     * @return  the code of this HTTPCode
     */
    public int getCode()
    {
        return this.code;
    }

    /**
     * Returns an HTTPCode from the given code
     * @param code      the given code
     * @return          HTTPCode corresponding to the given code
     */
    public static HTTPCode fromCode(int code)
    {
        for (HTTPCode httpCode : HTTPCode.values())
        {
            if (httpCode.getCode() == code)
            {
                return httpCode;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP code: " + code);
    }
}
