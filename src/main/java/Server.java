import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int PORT_NUMBER = 5000;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter outToClient;
    private BufferedReader inFromClient;

    public void startServerConnection() {
        try (serverSocket =new ServerSocket(PORT_NUMBER)){
            clientSocket = serverSocket.accept();

            System.out.println("Client connected");

            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter((clientSocket.getOutputStream(), true));

            String msg = inFromClient.readLine();
            if ("password2024".equals(msg)) {
                System.out.println("You are logged in");
            } else {
                System.out.println("Invalid password. Try again");
            }
        }

        public void stopConnection() {
            inFromClient.close();
            outToClient.close();
            serverSocket.close();
            clientSocket.close();
        }
    }
}
