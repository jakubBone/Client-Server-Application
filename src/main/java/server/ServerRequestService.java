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

     /*private AuthHandler authHandler;
     private ServerDetailsHandler serverInfoHandler;
     private MailboxHandler mailboxHandler;
     private AccountUpdateHandler updateHandler;
     private WriteHandler writeHandler;
     private LogoutHandler logoutHanlder;
     private AdminSwitchHandler switchHandlerResponse;*/
     private HandlerFactory handler;


    public ServerRequestService(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.handler = new HandlerFactory();

    }

    /*public void initializeHandlers(){
        this.authHandler = new AuthHandler();
        this.serverInfoHandler = new ServerDetailsHandler();
        this.mailboxHandler = new MailboxHandler();
        this.updateHandler = new AccountUpdateHandler();
        this.writeHandler = new WriteHandler();
        this.switchHandlerResponse = new AdminSwitchHandler();
        this.logoutHanlder = new LogoutHandler();
    }*/

    public void handleClientRequest() {
        String request;
        String response;
        try {
            while ((request = inFromClient.readLine()) != null) {
                Request req = getParseRequest(request);
                String requestCommand = req.getRequestCommand().toUpperCase();
                log.info("Handling request command: {}", requestCommand);
                switch (requestCommand) {
                    case "REGISTER":
                    case "LOGIN":
                        response = handler.getAuthHandler().getResponse(requestCommand, req.getUsername(), req.getPassword(), userManager);
                        break;
                    case "HELP":
                    case "INFO":
                    case "UPTIME":
                        response = handler.getServerInfoHandler().getResponse(requestCommand);
                        break;
                    case "WRITE":
                        response = handler.getWriteHandler().getResponse(req.getRecipient(), req.getMessage(), userManager);
                        break;
                    case "MAILBOX":
                        response = handler.getMailHandler().getResponse(req.getBoxOperation(),req.getBoxType());
                        break;
                    case "PASSWORD":
                        response = handler.getPasswordHandler().getResponse(req.getUserToUpdate(), req.getNewPassword(), userManager);
                        break;
                    case "DELETE":
                        response = handler.getDeleteHandler().getResponse(req.getUserToUpdate(), userManager);
                        break;
                    case "ROLE":
                        response = handler.getRoleHandler().getResponse(req.getUserToUpdate(), req.getNewRole(), userManager);
                        break;
                    case "SWITCH":
                        response = handler.getSwitchHandler().getResponse(req.getUserToSwitch(), userManager);
                        break;
                    case "LOGOUT":
                        response = handler.getLogoutHandler().getResponse(userManager);
                        break;
                    default:
                        log.warn("Unknown request command: {}", requestCommand);
                        response = "Unknown request command";
                        break;
                }
                sendResponse(response);
                log.info("Completed processing request command: {}", requestCommand);
            }
        } catch (IOException ex) {
            log.error("IOException occurred while processing the request: {}. Error: ", ex.getMessage());
        }
    }

    public Request getParseRequest(String request){
        log.info("Parsing request: {}", request);
        return gson.fromJson(request, Request.class);
    }
    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.serializeMessage();
        outToClient.println(json);
        log.info("Response sent: {}", json);
    }
}
