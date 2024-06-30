package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import handler.HandlerFactory;
import lombok.extern.log4j.Log4j2;
import request.Request;
import shared.JsonConverter;
import user.manager.UserManager;

 /**
  * The ServerRequestService class is responsible for handling various client requests
  * It manages user authentication, mail operations, user account updates, account switch, and server information
  */

@Log4j2
public class ServerRequestService {
    private final PrintWriter outToClient;
    private final BufferedReader inFromClient;
    private final UserManager userManager;
    private Gson gson;
    private JsonConverter jsonResponse;
     private HandlerFactory handler;


    public ServerRequestService(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.handler = new HandlerFactory();
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
         String command = req.getRequestCommand().toUpperCase();
         log.info("Handling request command: {}", command);
         switch (command) {
             case "REGISTER":
             case "LOGIN":
                 return handler.getAuthHandler().getResponse(command, req.getUsername(), req.getPassword(), userManager);
             case "HELP":
             case "INFO":
             case "UPTIME":
                 return handler.getServerInfoHandler().getResponse(command);
             case "WRITE":
                 return handler.getWriteHandler().getResponse(req.getRecipient(), req.getMessage(), userManager);
             case "MAILBOX":
                 return handler.getMailHandler().getResponse(req.getBoxOperation(), req.getBoxType());
             case "PASSWORD":
                 return handler.getPasswordHandler().getResponse(req.getUserToUpdate(), req.getNewPassword(), userManager);
             case "DELETE":
                 return handler.getDeleteHandler().getResponse(req.getUserToUpdate(), userManager);
             case "ROLE":
                 return handler.getRoleHandler().getResponse(req.getUserToUpdate(), req.getNewRole(), userManager);
             case "SWITCH":
                 return handler.getSwitchHandler().getResponse(req.getUserToSwitch(), userManager);
             case "LOGOUT":
                 return handler.getLogoutHandler().getResponse(userManager);
             default:
                 log.warn("Unknown request command: {}", command);
                 return "Unknown request command";
         }
     }

    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.serializeMessage();
        outToClient.println(json);
        log.info("Response sent: {}", json);
    }
}
