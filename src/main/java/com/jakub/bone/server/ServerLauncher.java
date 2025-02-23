package com.jakub.bone.server;

import com.jakub.bone.network.ServerConnectionManager;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class ServerLauncher {

    public static void main(String[] args) {
        try {
            ServerConnectionManager connManager = new ServerConnectionManager();
            connManager.connect("localhost", 5000);
            RequestProcessor processor = new RequestProcessor(connManager.getOut(), connManager.getIn());
            processor.start();
            connManager.disconnect();
        } catch (Exception e) {
            log.error("Error starting server on port {}: {}", 5000, e.getMessage());
        }
    }
}