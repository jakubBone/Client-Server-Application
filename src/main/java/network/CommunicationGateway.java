package network;

public interface CommunicationGateway {
    void sendMessage(String message);
    String receiveMessage();
    void disconnect();
}
