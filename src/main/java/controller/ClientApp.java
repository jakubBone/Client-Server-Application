package controller;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        try {
            ClientController controller = new ClientController();
            controller.start();
        } catch (Exception e) {
            System.err.println("Error starting client: " + e.getMessage());
        }
    }
}
