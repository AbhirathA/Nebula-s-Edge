package org.spaceinvaders;

import com.sun.net.httpserver.HttpServer;
import org.spaceinvaders.firebase.Firebase;
import org.spaceinvaders.handlers.GetDataHandler;
import org.spaceinvaders.handlers.SignUpHandler;
import org.spaceinvaders.util.LoggerUtil;
import org.spaceinvaders.util.ServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Server.java
 * <br>
 * The Server class represents a basic server that handles client requests
 * and manages client connections. This class is responsible for:
 * <ul>
 *      <li>Initializing and starting the server on a specified port.</li>
 *      <li>Accepting client connections and establishing communication channels.</li>
 *      <li>Handling incoming client requests and processing responses.</li>
 * </ul>
 * @author Aryan
 * @author Gathik
 * @author Abhirath
 * @author Ibrahim
 * @author Jayant
 * @author Dedeepya
 * @version 1.0
 * @since 11/13/2024
 */
public class Server {

    /**
     * The max number of queued incoming connections allowed
     */
    public static final int BACKLOG_LIMIT = 50;

    private Server() {}

    /**
     * Runs the program/server
     * @param args command line argument
     */
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(ServerInfo.HTTP_PORT), BACKLOG_LIMIT);
            UDPServer udpServer = new UDPServer();
            //noinspection ResultOfMethodCallIgnored
            Firebase.getInstance();
            server.createContext("/signup", new SignUpHandler());
            server.createContext("/getData", new GetDataHandler());
            server.setExecutor(null);
            server.start();
            udpServer.startThreads();
            LoggerUtil.logInfo("Server started at http://localhost:" + ServerInfo.HTTP_PORT);
        } catch (IOException e) {
            LoggerUtil.logException("Cannot create a localhost server on port " + ServerInfo.HTTP_PORT, e);
        }
    }
}
