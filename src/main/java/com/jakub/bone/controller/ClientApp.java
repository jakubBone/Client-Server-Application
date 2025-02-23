package com.jakub.bone.controller;

import com.jakub.bone.network.ClientConnectionManager;
import com.jakub.bone.network.Messenger;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        try {
            ClientConnectionManager connManager = new ClientConnectionManager();
            connManager.connect("localhost", 5000);

            Messenger messenger = new Messenger(connManager.getOut(), connManager.getIn());

            ClientController controller = new ClientController(messenger);
            controller.start();

            connManager.disconnect();
        } catch (Exception e) {
            System.err.println("Error starting client: " + e.getMessage());
        }
    }
}
