package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import handler.auth.AuthHandler;
import handler.auth.LogoutHandler;
import handler.mail.MailboxHandler;
import handler.mail.WriteHandler;
import handler.server.ServerDetailsHandler;
import handler.user.AccountUpdateHandler;
import handler.user.AdminSwitchHandler;
import lombok.extern.log4j.Log4j2;
import request.Request;
import shared.JsonConverter;
import user.UserManager;

 /*
  * The ServerRequestService class is responsible for handling various client requests and processing server-side logic
  * It manages user authentication, mail operations, user account updates, account switch, and server information
  */

@Log4j2
public class ServerRequestService {
    private final PrintWriter outToClient;
    private final BufferedReader inFromClient;
    private final UserManager userManager;
    private Gson gson;
    private JsonConverter jsonResponse;
    private AuthHandler authHandler;
    private ServerDetailsHandler serverInfoHandler;
    private MailboxHandler mailboxHandler;
    private AccountUpdateHandler updateHandler;
    private WriteHandler writeHandler;
    private LogoutHandler logoutHandler;
    private AdminSwitchHandler switchHandler;

    public ServerRequestService(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.authHandler = new AuthHandler();
        this.serverInfoHandler = new ServerDetailsHandler();
        this.mailboxHandler = new MailboxHandler();
        this.updateHandler = new AccountUpdateHandler();
        this.writeHandler = new WriteHandler();
        this.switchHandler = new AdminSwitchHandler();
        this.logoutHandler = new LogoutHandler();
    }

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
                        response = authHandler.getResponse(requestCommand, req.getUsername(), req.getPassword(), userManager);
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
                        response = mailboxHandler.getResponse(req.getBoxOperation(),req.getBoxType(), userManager);
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

    public void closeDataBase() {
        if (userManager != null) {
            userManager.close();
            log.info("Data Base disconnected");
        }
    }
}
