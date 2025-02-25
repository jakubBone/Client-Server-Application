package com.jakub.bone.network;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Log4j2
@Getter
public class ClientConnectionManager implements ConnectionManager{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void connect(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            log.error("Error while closing socket: {}", e.getMessage());
        }
    }
}
