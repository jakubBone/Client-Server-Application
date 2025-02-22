package com.jakub.bone.network;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

@Log4j2
public class SocketServerGateway implements CommunicationGateway{
    public static Date startTime;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketServerGateway(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        log.info("Server started on port {}", port);
        this.clientSocket = serverSocket.accept();
        log.info("Client connected: {}", clientSocket.getRemoteSocketAddress());
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        startTime = new Date();
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
            while ((line = in.readLine()) != null && !line.equals("<<END>>")) {
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
            log.info("Server disconnected");
        } catch (IOException e) {
            log.error("Error disconnecting: {}", e.getMessage());
        }
    }
}
