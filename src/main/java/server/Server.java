package server;

import database.DataBase;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Server {
    private static final int PORT = 5000;
    public static void main(String[] args) {
        log.info("Starting server on port {}", PORT);
        ServerConnection connectionHandler = new ServerConnection(PORT);
        connectionHandler.startServer();
        DataBase dataBase = new DataBase();
        ServerRequestService logicHandler = new ServerRequestService(
                connectionHandler.getOutToClient(),
                connectionHandler.getInFromClient());

        logicHandler.handleClientRequest();

        connectionHandler.closeConnections();
    }
}