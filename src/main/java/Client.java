import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final int PORT_NUMBER = 5000;

    public Client(){
        Client client = new Client();
        client.connectToServer();
    }

    public void connectToServer() {
        try(Socket clientSocket = new Socket("local host", PORT_NUMBER);
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){

            System.out.println("Enter \"HELP\" for instruction");

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }



}
