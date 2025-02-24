package com.jakub.bone.utils;

import com.jakub.bone.utils.JsonConverter;

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
            e.printStackTrace();
        }
        String json = builder.toString();
        return JsonConverter.deserialize(json, clazz);
    }
}

