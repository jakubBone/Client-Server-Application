package com.jakub.bone.server;

import com.jakub.bone.network.ServerConnectionManager;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class ServerApp {

    public static void main(String[] args) {
        try {
            ServerConnectionManager connManager = new ServerConnectionManager();
            connManager.connect("localhost", 5000);
            ServerRequestHandler requestHandler = new ServerRequestHandler(connManager.getOut(), connManager.getIn());
            requestHandler.start();
            connManager.disconnect();
        } catch (Exception e) {
            log.error("Error starting server on port {}: {}", 5000, e.getMessage());
        }
    }
}