package com.jakub.bone.server;

import lombok.extern.log4j.Log4j2;
import com.jakub.bone.network.SocketServerGateway;

@Log4j2
public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            SocketServerGateway gateway = new SocketServerGateway(PORT);
            ServerRequestService requestService = new ServerRequestService(gateway);
            requestService.handleClientRequest();
        } catch (Exception e) {
            log.error("Error starting server on port {}: {}", PORT, e.getMessage());
        }
    }
}