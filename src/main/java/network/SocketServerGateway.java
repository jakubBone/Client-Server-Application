package network;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Log4j2
public class SocketServerGateway implements CommunicationGateway{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketServerGateway(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log.info("Server started on port {}", port);
        clientSocket = serverSocket.accept();
        log.info("Client connected: {}", clientSocket.getRemoteSocketAddress());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public String receiveMessage() {
        try {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null)) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            log.error("Error receiving message: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void disconnect() {
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            log.error("Error disconnecting: {}", e.getMessage());
        }
    }
}
