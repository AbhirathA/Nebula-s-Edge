package com.spaceinvaders.backend.firebase.utils;

/**
 * HttpResponse.java
 * <br>
 * This class represents an HTTP response, encapsulating the HTTP response code and message body.
 * It is used as a return type for methods that handle HTTP requests, providing a structured way
 * to interpret and utilize server responses.
 * Key features:
 * - Stores the HTTP response code (e.g., 200 for success, 404 for not found).
 * - Stores the HTTP response body or error message as a string.
 * - Provides getter methods to access the response code and message.
 * Usage example:
 * ```
 * HttpResponse response = new HttpResponse(200, "Success");
 * System.out.println("Code: " + response.getCode());
 * System.out.println("Message: " + response.getMessage());
 * ```
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/21/2024
 */
public class HttpResponse {
    private final int code; // The HTTP response code (e.g., 200, 404)
    private final String message; // The HTTP response message body or error details

    /**
     * Constructs an HttpResponse with the specified code and message.
     *
     * @param code    The HTTP response code.
     * @param message The HTTP response message body or error details.
     */
    public HttpResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Retrieves the HTTP response code.
     *
     * @return The HTTP response code as an Integer.
     */
    public Integer getCode()
    {
        return this.code;
    }

    /**
     * Retrieves the HTTP response message.
     *
     * @return The HTTP response message as a String.
     */
    public String getMessage()
    {
        return this.message;
    }
}
