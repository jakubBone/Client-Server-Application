package com.jakub.bone.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketGateway implements CommunicationGateway {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketGateway(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            // log error, throw runtime exception
            return null;
        }
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            // log error
        }
    }
}
