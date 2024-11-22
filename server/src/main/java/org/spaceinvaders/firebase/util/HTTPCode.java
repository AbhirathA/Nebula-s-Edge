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

package org.spaceinvaders.firebase.util;

public enum HTTPCode {
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

    /**
     * @param code the http code
     */
    private HTTPCode(int code) {
        this.code = code;
    }

    /**
     * Returns the http code of this enum
     * 
     * @return the http code
     */
    public int getCode() {
        return this.code;
    }
}
