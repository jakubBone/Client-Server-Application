package servercommand;

import com.google.gson.Gson;
import command.CommandMessage;

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
            // Odczytujemy komunikat zakończony znakiem specjalnym, np. "<<END>>"
            while ((line = in.readLine()) != null && !line.equals("<<END>>")) {
                jsonBuilder.append(line);
            }
            String jsonCommand = jsonBuilder.toString();
            // Deserializacja JSON do obiektu CommandMessage
            CommandMessage commandMessage = gson.fromJson(jsonCommand, CommandMessage.class);
            // Pobieramy odpowiedni handler
            ServerCommand serverCommand = commandFactory.createCommand(commandMessage);
            // Wykonujemy komendę
            String result = serverCommand.execute(commandMessage);
            // Wysyłamy wynik z powrotem do klienta
            out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
