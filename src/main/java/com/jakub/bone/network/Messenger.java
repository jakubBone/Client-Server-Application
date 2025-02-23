package com.jakub.bone.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Messenger {
    private PrintWriter out;
    private BufferedReader in;

    public Messenger(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void send(String message) {
        out.println(message);
    }

    public String receive() {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null && !line.equals("<<END>>")) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}

