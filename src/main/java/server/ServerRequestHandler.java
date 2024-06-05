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
    private AccountUpdateHandler accountUpdateHandler;
    private WriteHandler writeHandler;

    public ServerRequestHandler(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.gson = new Gson();
        this.credentialHandler= new CredentialHandler();
        this.serverInfoHandler = new ServerInfoHandler();
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
                String requestCommand = reqFromJson.getRequestCommand().toUpperCase();
                log.info("Handling request request command: {}", requestCommand);
                switch (requestCommand) {
                    case "REGISTER":
                    case "LOGIN":
                        response = credentialHandler.getCredentialResponse(requestCommand,
                                reqFromJson.getUsername(), reqFromJson.getPassword(), userManager);
                        break;
                    case "HELP":
                    case "INFO":
                    case "UPTIME":
                        response = serverInfoHandler.getServerInfoResponse(requestCommand);
                        break;
                    case "WRITE":
                        response = writeHandler.getWriteResponse(reqFromJson.getRecipient(),
                                reqFromJson.getMessage(), userManager);
                        break;
                    case "MAILBOX":
                        response = mailboxHandler.getMailboxResponse(reqFromJson.getBoxOperation(),
                                reqFromJson.getMailbox());
                        break;
                    case "UPDATE":
                        response = accountUpdateHandler.getUpdateResponse(reqFromJson.getUpdateOperation(),
                                reqFromJson.getUserToUpdate(), reqFromJson.getNewPassword(), userManager);
                        break;
                    case "LOGOUT":
                        response = credentialHandler.getLogoutResponse(userManager);
                        break;
                }

                sendResponse(response);
                log.info("Completed authentication requestCommand: {}", requestCommand);
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
