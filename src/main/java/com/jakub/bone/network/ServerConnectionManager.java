package com.jakub.bone.network;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

@Log4j2
@Getter
public class ServerConnectionManager implements ConnectionManager{
    public static Date startTime;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void connect(String host, int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        log.info("Server started on port {}", port);
        this.clientSocket = serverSocket.accept();
        log.info("Client connected: {}", clientSocket.getRemoteSocketAddress());
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        startTime = new Date();
    }

    @Override
    public void disconnect() {
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            log.error("Error while closing server sockets: {}", e.getMessage());
        }
    }
}
