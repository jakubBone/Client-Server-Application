package com.jakub.bone.server;

import com.jakub.bone.network.ServerConnectionManager;
import com.jakub.bone.utils.ConfigLoader;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class ServerLauncher {
    public static void main(String[] args) {
        ServerConnectionManager connManager = null;
        try {
            String host = ConfigLoader.get("server.host");
            int port = Integer.parseInt(ConfigLoader.get("server.port"));

            connManager = new ServerConnectionManager();
            connManager.connect(host, port);

            RequestProcessor processor = new RequestProcessor(connManager.getOut(), connManager.getIn());
            processor.start();
            connManager.disconnect();
        } catch (Exception e) {
            log.error("Error starting server on port {}: {}", ConfigLoader.get("server.port"), e.getMessage());
        } finally {
            if (connManager != null) {
                try {
                    connManager.disconnect();
                    log.info("Server disconnected successfully");
                } catch (Exception e) {
                    log.error("Error during server disconnect: {}", e.getMessage(), e);
                }
            }
        }
    }
}