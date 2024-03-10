import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    private Date serverTimeCreation = new Date();
    private final int PORT = 5000;
    private final String VERSION = "1.0.0";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    BufferedReader inFromClient;
    PrintWriter outToClient;

    public static void main(String[] args)  {
        Server server = new Server();
    }

    public Server() {
        establishServerConnection();
        handleClientRequest();
    }

    public void establishServerConnection() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");
            clientSocket = serverSocket.accept();
            System.out.println("Connection established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void handleClientRequest(){
        try{
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

                while(true){
                    String request = inFromClient.readLine().toUpperCase();
                    if(request.equals("STOP")){
                        disconnect();
                        break;
                    }
                    sendResponse(request, outToClient);
                }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void sendResponse(String clientRequest, PrintWriter outToClient) throws IOException {
        switch (clientRequest) {
            case "UPTIME":
                Date currentTime = new Date();
                System.out.println("Time from server setup: " + (currentTime.getTime() - serverTimeCreation.getTime()));
                outToClient.println("UPTIME returned");
                break;
            case "INFO":
                System.out.println("Server version: " + VERSION);
                System.out.println("Setup date: " + serverTimeCreation);
                outToClient.println("INFO returned");
                break;
            case "HELP":
                System.out.println("Help Menu: ");
                System.out.println("UPTIME - opis");
                System.out.println("INFO - opis");
                System.out.println("HELP - opis");
                System.out.println("STOP - opis");
                outToClient.println("HELP returned");
                break;
            default:
                System.out.println("Invalid request");
                outToClient.println("INVALID returned");
        }
    }

    public void disconnect()  {
        try {
            inFromClient.close();
            outToClient.close();
            serverSocket.close();
            clientSocket.close();
            System.out.println("Connection stopped");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


