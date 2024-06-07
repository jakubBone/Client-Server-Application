package server;

public class Server {
    private static final int PORT = 5000;
    public static void main(String[] args) {
        ServerConnection connectionHandler = new ServerConnection(PORT);
        connectionHandler.startServer();

        ServerRequestService logicHandler = new ServerRequestService(
                connectionHandler.getOutToClient(),
                connectionHandler.getInFromClient());

        logicHandler.handleClientRequest();

        connectionHandler.closeConnections();
    }
}