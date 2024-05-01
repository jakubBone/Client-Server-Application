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
    public static boolean isAuthorized;
    private JsonConverter jsonResponse;
    private CredentialHandler credentialHandler
    private HelpHandler helpHandler
    private MailboxHandler mailboxHandler
    private AccountUpdateHandler accountUpdateHandler
    private WriteHandler writeHandler

    public ServerRequestHandler(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.credentialHandler= new CredentialHandler();
        this.helpHandler = new HelpHandler();
        this.mailboxHandler = new MailboxHandler();
        this.accountUpdateHandler = new AccountUpdateHandler();
        this.writeHandler = new WriteHandler();
    }

    public void handleClientRequest() {
        String request = null;
        String response = null;
        try {
            while ((request = inFromClient.readLine()) != null) {
                Request reqFromJson = gson.fromJson(request, Request.class);

                String command = reqFromJson.getRequestCommand().toUpperCase();
                log.info("Handling command: {}", command);
                switch (command) {
                    case "REGISTER":
                    case "LOGIN":
                        response = credentialHandler.getAuthResponse(command, reqFromJson.getUsername(), reqFromJson.getPassword(), userManager);
                        break;
                    case "HELP":
                        response = helpHandler.getHelpRequest(command);
                        break;
                    case "WRITE":
                        response = writeHandler.getWriteResponse(reqFromJson.getRecipient(), reqFromJson.getMessage(), userManager);
                        break;
                    case "MAILBOX":
                        response = mailboxHandler.getMailboxResponse(reqFromJson.getBoxOperation(), reqFromJson.getMailbox());
                        break;
                    case "UPDATE":
                        response = accountUpdateHandler.getUpdateStatus(userManager);
                        sendResponse(response);
                        if(isAuthorized) {
                            request = inFromClient.readLine();
                            reqFromJson = gson.fromJson(request, Request.class);
                            response = accountUpdateHandler.getUpdateResponse(reqFromJson, userManager);
                            isAuthorized = false;
                        }
                        break;
                    case "LOGOUT":
                        response = credentialHandler.getLogoutResponse(userManager);
                        break;
                }
                sendResponse(response);
                log.info("Completed authentication command: {}", command);
            }
        } catch (IOException ex) {
            log.error("IOException occurred while processing the request: {}. Error: ", request, ex);
        }
    }

    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.serializeMessage();
        outToClient.println(json);
        log.info("Response sent: {}", json);
    }
}
