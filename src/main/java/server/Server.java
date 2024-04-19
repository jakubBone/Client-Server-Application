package server;

import java.util.*;

public class Server {
    private final String VERSION = "1.0.0";
    private static final int PORT = 5000;
    private Date serverTimeCreation = new Date();

    public static void main(String[] args) {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(PORT);
        connectionHandler.startServer();

        ServerLogicHandler logicHandler = new ServerLogicHandler(
                connectionHandler.getOutToClient(),
                connectionHandler.getInFromClient());

        logicHandler.handleClientRequest();

        connectionHandler.closeConnections();

    }
}