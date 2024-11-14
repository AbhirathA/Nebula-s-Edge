package org.spaceinvaders.util;

public class NetworkNotFoundException extends RuntimeException
{
    public NetworkNotFoundException(String message)
    {
        super(message);
    }
}
