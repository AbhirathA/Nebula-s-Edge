/**
 * LoggerUtil.java
 * The LoggerUtil class represents a basic utility class that handles all the server
 * logging that is required.
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/15/2024
 */

package org.spaceinvaders.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil
{
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    private LoggerUtil() {}

    /**
     * Logs an information message to the log file
     * @param message       the message to log
     */
    public static void logInfo(String message)
    {
        logger.info(message);
    }

    /**
     * Logs a warning message to the log file
     * @param message       the message to log
     */
    public static void logWarning(String message)
    {
        logger.warn(message);
    }

    /**
     * Logs an error message to the log file
     * @param message       the message to log
     */
    public static void logError(String message)
    {
        logger.error(message);
    }

    /**
     * Logs an exception to the log file
     * @param message       the message to log
     * @param e             the exception to log
     */
    public static void logException(String message, Exception e)
    {
        logger.error(message, e);
    }
}
