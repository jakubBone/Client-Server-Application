package server;

import java.io.IOException;

import network.CommunicationGateway;
import response.Response;
import response.ResponseFactory;
import lombok.extern.log4j.Log4j2;
import mail.MailService;
import user.manager.AuthManager;
import user.manager.UserManager;

@Log4j2
public class ServerRequestService {
    private final CommunicationGateway gateway;
    private final AuthManager authManager;
    private final UserManager userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;

    public ServerRequestService(CommunicationGateway gateway) {
        this.gateway = gateway;
        this.authManager = new AuthManager();
        this.userManager = new UserManager();
        this.mailService = new MailService();
        this.serverDetails = new ServerDetails();
    }

    public void handleClientRequest() {
        try {
            while (true) {
                String jsonRequest = gateway.receiveMessage();
                if (jsonRequest == null || jsonRequest.isEmpty()) break;
                log.info("Received JSON request: {}", jsonRequest);
                ServerRequest request = JsonUtil.deserialize(jsonRequest, ServerRequest.class);
                ServerCommand command = commandFactory.createCommand(request);
                String result = command.execute();
                String jsonResponse = JsonUtil.serialize(result) + "\n<<END>>";
                gateway.sendMessage(jsonResponse);


                /*String jsonRequest = gateway.receiveMessage();
                if (jsonRequest == null || jsonRequest.isEmpty()) break;
                log.info("Received JSON request: {}", jsonRequest);
                String request = JsonConverter.deserialize(jsonRequest, String.class);
                String response = processRequest(request)
                String jsonResponse = JsonConverter.serialize(response) + "\n<<END>>";
                gateway.sendMessage(jsonResponse);*/
            }
        } catch (IOException e) {
            log.error("Error handling client request: {}", e.getMessage());
        } finally {
            gateway.disconnect();
        }
    }

     public String processRequest(String request) throws IOException{
         String requestCommand = request.toUpperCase();
         ResponseFactory factory = new ResponseFactory(authManager, userManager,mailService, serverDetails);
         Response command = factory.getResponse(requestCommand);
         log.info("Handling request command: {}", command.toString());
         return command.execute(req);
     }
}
