package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import response.Response;
import response.ResponseFactory;
import handler.HandlerFactory;
import lombok.extern.log4j.Log4j2;
import mail.MailService;
import request.Request;
import shared.JsonConverter;
import user.manager.AuthManager;
import user.manager.UserManager;

@Log4j2
public class ServerRequestService {
    private final PrintWriter outToClient;
    private final BufferedReader inFromClient;
    private final UserManager userManager;
    private Gson gson;
    private JsonConverter jsonResponse;
    private HandlerFactory handler;
    private AuthManager authManager;
    private ServerDetails serverDetails;
    private MailService mailService;


    public ServerRequestService(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.handler = new HandlerFactory();

        this.authManager = new AuthManager();
        this.mailService = new MailService();
        this.serverDetails = new ServerDetails();
    }

    public void handleClientRequest() {
        String request;
        try {
            while ((request = inFromClient.readLine()) != null) {
                Request req = getParseRequest(request);
                String response = processRequest(req);
                sendResponse(response);
            }
        } catch (IOException ex) {
            log.error("IOException occurred while processing the request: {}. Error: ", ex.getMessage());
        }
    }

    public Request getParseRequest(String request){
        log.info("Parsing request: {}", request);
        return gson.fromJson(request, Request.class);
    }

     public String processRequest(Request req) throws IOException{
         String requestCommand = req.getRequestCommand().toUpperCase();
         ResponseFactory factory = new ResponseFactory(authManager, userManager,mailService, serverDetails);
         Response command = factory.getResponse(requestCommand);
         log.info("Handling request command: {}", command.toString());
         return command.execute(req);
     }

    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.serializeMessage();
        outToClient.println(json);
        log.info("Response sent: {}", json);
    }
}
