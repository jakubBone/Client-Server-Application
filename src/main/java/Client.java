import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final int PORT_NUMBER = 5000;
    Socket clientSocket;
    PrintWriter outToServer;
    BufferedReader inFromServer;
    BufferedReader userInput;

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client(){
        connectToServer();
        handleServerCommunication();
    }

    public void connectToServer() {
        try {
            clientSocket = new Socket("localhost", PORT_NUMBER);
            System.out.println("Connection with Server established");
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void handleServerCommunication(){
        try {
            outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Type \"HELP\" to enter COMMANDS MENU: ");

            String request = userInput.readLine().toUpperCase();
            while(request != null) {
                outToServer.println(request);
                String response = inFromServer.readLine();
                System.out.println("Server response: " + request);
                if(request.equals("STOP")){
                    System.out.println("Client disconnected");
                    disconnect();
                    break;
                }
            }
        } catch (IOException ex){
            ex.getStackTrace();
        }
    }

    public void disconnect() {
        try {
            outToServer.close();
            inFromServer.close();
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
