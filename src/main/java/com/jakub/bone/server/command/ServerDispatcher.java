package com.jakub.bone.server.command;

import com.google.gson.Gson;
import com.jakub.bone.client.command.CommandMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerDispatcher {
    private final BufferedReader in;
    private final PrintWriter out;
    private final ServerCommandFactory commandFactory;
    private final Gson gson = new Gson();

    public ServerDispatcher(BufferedReader in, PrintWriter out, ServerCommandFactory commandFactory) {
        this.in = in;
        this.out = out;
        this.commandFactory = commandFactory;
    }

    public void processClientRequests() {
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.equals("<<END>>")) {
                jsonBuilder.append(line);
            }

            String jsonCommand = jsonBuilder.toString();

            CommandMessage commandMessage = gson.fromJson(jsonCommand, CommandMessage.class);

            ServerCommand serverCommand = commandFactory.createCommand(commandMessage);

            String result = serverCommand.execute(commandMessage);

            out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
