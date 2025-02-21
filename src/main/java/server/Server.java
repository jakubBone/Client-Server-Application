package server;

import lombok.extern.log4j.Log4j2;
import network.SocketServerGateway;

@Log4j2
public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            SocketServerGateway gateway = new SocketServerGateway(PORT);
            ServerRequestService requestService = new ServerRequestService(gateway);
            requestService.handleClientRequest();
        } catch (Exception e) {
            log.error("Error starting server: {}", e.getMessage());
        }
    }



    /*private static final int PORT = 5000;
    public static void main(String[] args) {
        log.info("Starting server on port {}", PORT);
        ServerConnection connectionHandler = new ServerConnection(PORT);
        connectionHandler.startServer();
        ServerRequestService logicHandler = new ServerRequestService(
                connectionHandler.getOutToClient(),
                connectionHandler.getInFromClient());

        logicHandler.handleClientRequest();

        try {
            logicHandler.handleClientRequest();
        } finally {
            connectionHandler.closeConnections();
            DatabaseConnection.getInstance().disconnect();
        }

    }*/
}