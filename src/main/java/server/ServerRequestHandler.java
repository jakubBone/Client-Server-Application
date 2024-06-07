package server;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import operations.*;
import request.Request;
import user.UserManager;
import utils.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

 /*
  * The ServerLogicHandler class is responsible for handling various client requests and processing server-side logic
  * It manages user authentication, mail operations, and server information
  */

@Log4j2
public class ServerRequestHandler {
    private final PrintWriter outToClient;
    private final BufferedReader inFromClient;
    private final UserManager userManager;
    private Gson gson;
    private JsonConverter jsonResponse;
    private CredentialHandler credentialHandler;
    private ServerInfoHandler serverInfoHandler;
    private MailboxHandler mailboxHandler;
    private AccountUpdateHandler updateHandler;
    private WriteHandler writeHandler;
    private LogoutHandler logoutHandler;
    private AdminSwitchUserHandler switchHandler;
    public ServerRequestHandler(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.credentialHandler= new CredentialHandler();
        this.serverInfoHandler = new ServerInfoHandler();
        this.mailboxHandler = new MailboxHandler();
        this.updateHandler = new AccountUpdateHandler();
        this.writeHandler = new WriteHandler();
        this.switchHandler = new AdminSwitchUserHandler();
        this.logoutHandler = new LogoutHandler();
    }

    public void handleClientRequest() {
        String request = null;
        String response = null;
        try {
            while ((request = inFromClient.readLine()) != null) {
                Request req = getParseRequest(request);
                String requestCommand = req.getRequestCommand().toUpperCase();
                log.info("Handling request request command: {}", requestCommand);
                switch (requestCommand) {
                    case "REGISTER":
                    case "LOGIN":
                        response = credentialHandler.getResponse(requestCommand, req.getUsername(), req.getPassword(), userManager);
                        break;
                    case "HELP":
                    case "INFO":
                    case "UPTIME":
                        response = serverInfoHandler.getResponse(requestCommand);
                        break;
                    case "WRITE":
                        response = writeHandler.getResponse(req.getRecipient(), req.getMessage(), userManager);
                        break;
                    case "MAILBOX":
                        response = mailboxHandler.getResponse(req.getBoxOperation(),req.getMailbox());
                        break;
                    case "PASSWORD":
                        response = updateHandler.getChangePasswordResponse(req.getUserToUpdate(), req.getNewPassword(), userManager);
                        break;
                    case "DELETE":
                        response = updateHandler.getUserDeleteResponse(req.getUserToUpdate(), userManager);
                        break;
                    case "ROLE":
                        response = updateHandler.getChangeRoleResponse(req.getUserToUpdate(), req.getNewRole(), userManager);
                        break;
                    case "SWITCH":
                        response = switchHandler.getResponse(req.getUserToSwitch(), userManager);
                        break;
                    case "LOGOUT":
                        response = logoutHandler.getResponse(userManager);
                        break;
                }
                sendResponse(response);
                log.info("Completed authentication requestCommand: {}", requestCommand);
            }
        } catch (IOException ex) {
            log.error("IOException occurred while processing the request: {}. Error: ", request, ex);
        }
    }

    public Request getParseRequest(String request){
        return gson.fromJson(request, Request.class);
    }
    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.serializeMessage();
        outToClient.println(json);
        log.info("Response sent: {}", json);
    }
}
