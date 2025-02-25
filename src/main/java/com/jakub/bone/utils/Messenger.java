package com.jakub.bone.utils;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class Messenger {
    private PrintWriter out;
    private BufferedReader in;

    public Messenger(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void send(Object message) {
        String json = JsonConverter.serialize(message) + "\n<<END>>";
        out.println(json);
    }


    public <T> T receive(Class<T> clazz) {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null && !line.equals("<<END>>")) {
                builder.append(line);
            }
        } catch (IOException e) {
            log.error("Error while reading from input stream: {}", e.getMessage());
            throw new RuntimeException("Error reading message", e);
        }
        String json = builder.toString();
        return JsonConverter.deserialize(json, clazz);
    }
}

