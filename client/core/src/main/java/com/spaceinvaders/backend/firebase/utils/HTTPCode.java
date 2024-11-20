package com.spaceinvaders.backend.firebase.utils;

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

    private HTTPCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return this.code;
    }
}
