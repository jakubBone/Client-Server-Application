package com.jakub.bone.controller;

import com.jakub.bone.network.ClientConnectionManager;
import com.jakub.bone.utils.Messenger;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class ClientApp {
    public static void main(String[] args) throws IOException {
        ClientConnectionManager connManager = null;
        try {
            connManager = new ClientConnectionManager();
            connManager.connect("localhost", 5000);

            Messenger messenger = new Messenger(connManager.getOut(), connManager.getIn());
            ClientController controller = new ClientController(messenger);
            controller.start();
        } catch (Exception e) {
            log.error("Error starting client: {}", e.getMessage(), e);
            System.err.println("Error starting : " + e.getMessage());
        } finally {
            if (connManager != null) {
                try {
                    connManager.disconnect();
                } catch (Exception e) {
                    log.error("Error during client disconnect: {}", e.getMessage(), e);
                }
            }
        }
    }
}
