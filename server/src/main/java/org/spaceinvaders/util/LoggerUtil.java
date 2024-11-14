package org.spaceinvaders.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil
{
    private static final Logger logger;
    public static final String LOG_FILE = "../serverLog.log";

    static
    {
        logger = Logger.getLogger(LoggerUtil.class.getName());

        try
        {
            FileHandler filehandler = new FileHandler(LOG_FILE, true);
            filehandler.setFormatter(new SimpleFormatter());
            logger.addHandler(filehandler);
            logger.setUseParentHandlers(false); //disables console logging
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, "Failed to open log file", e);
        }
    }

    private LoggerUtil() {}

    public static void logInfo(String message)
    {
        logger.log(Level.INFO, message);
    }

    public static void logWarning(String message)
    {
        logger.log(Level.WARNING, message);
    }

    public static void logError(String message)
    {
        logger.log(Level.SEVERE, message);
    }

    public static void logException(String message, Exception e)
    {
        logger.log(Level.SEVERE, message, e);
    }
}
