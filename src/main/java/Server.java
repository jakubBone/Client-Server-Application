import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    private Date serverTimeCreation = new Date();
    private final int PORT_NUMBER = 5000;
    private final String VERSION = "1.0.0";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    BufferedReader inFromClient;
    PrintWriter outToClient;

    private Server() {
        establishServerConnection();
        handleClientRequest();
    }

    public void establishServerConnection() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            clientSocket = serverSocket.accept();
            System.out.println("Client connected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void handleClientRequest() {
        inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToClient = new PrintWriter((clientSocket.getOutputStream(), true));

        String request = inFromClient.readLine();
        while(request == null) {
            System.out.println("CLient reguest: " + request);
            handleRequest(request, outToClient);
        }
    }

    public void handleRequest(String clientRequest, PrintWriter outToClient){
        switch (clientRequest) {
            case "UPTIME":
                Date currentTime = new Date();
                System.out.println("Time from server setup: " currentTime.getTime() - serverTimeCreation.getTime());
                //outToClient.println(JSON);
                break;
            case "INFO":
                System.out.println("Server version: " + VERSION);
                System.out.println("Setup date: " + serverTimeCreation);
                //outToClient.println(JSON);
                break;
            case "HELP":
                System.out.println("Help Menu: ");
                System.out.println("UPTIME - opis");
                System.out.println("INFO - opis");
                System.out.println("HELP - opis");
                System.out.println("STOP - opis");
                //outToClient.println(JSON);
                break;
            case "STOP":
                System.out.println("Connection stopped");
                //outToClient.println(JSON);
                stopConnection();
                break;
        }
    }
    public void stopConnection() {
        inFromClient.close();
        outToClient.close();
        serverSocket.close();
        clientSocket.close();
    }
}


