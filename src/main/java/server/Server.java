package server;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(PORT);
        connectionHandler.startServer();

        ServerRequestHandler logicHandler = new ServerRequestHandler(
                connectionHandler.getOutToClient(),
                connectionHandler.getInFromClient());

        logicHandler.handleClientRequest();

        connectionHandler.closeConnections();
    }
}