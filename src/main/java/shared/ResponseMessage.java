package shared;

import lombok.extern.log4j.Log4j2;

/**
 * The ResponseMessage enum defines various response
 * The messages is sent from the server to the client.
 */
@Log4j2
public enum ResponseMessage {

    // General responses
    OPERATION_SUCCEEDED("Operation succeeded"),
    OPERATION_FAILED("Operation failed"),
    FAILED_TO_FIND_USER("Failed to find user"),

    // Register responses
    REGISTRATION_SUCCESSFUL("Registration successful"),
    REGISTRATION_FAILED_USER_EXISTS("Registration failed: User already exists"),

    // Login responses
    USER_LOGIN_SUCCEEDED("User login successful"),
    ADMIN_LOGIN_SUCCEEDED("Admin login successful"),
    LOGIN_FAILED_INCORRECT_PASSWORD("Login failed: Incorrect password"),

    // Logout response
    LOGOUT_SUCCEEDED("Logout succeeded"),

    // Authorization responses
    AUTHORIZATION_SUCCEEDED("Authorization succeeded"),
    AUTHORIZATION_FAILED("Authorization failed"),

    // Account switch responses
    SWITCH_SUCCEEDED("Switch succeeded"),
    SWITCH_FAILED("Switch failed"),

    // Role change responses
    ROLE_CHANGE_SUCCEEDED("Role change succeeded"),
    ROLE_CHANGE_FAILED("Role change failed"),

    // Mail responses
    SENDING_SUCCEEDED("Sending succeeded"),
    SENDING_FAILED_BOX_FULL("Sending failed: Recipient's mailbox is full"),
    SENDING_FAILED_TO_LONG_MESSAGE("Sending failed: Message too long (maximum 255 characters)"),
    SENDING_FAILED_RECIPIENT_NOT_FOUND("Sending failed: Recipient not found"),
    MAIL_DELETION_SUCCEEDED("Mails deletion succeeded"),
    MAILBOX_EMPTY("Mailbox is empty"),

    // Unknown Message
    UNKNOWN_REQUEST("Unknown request"),
    UNKNOWN_RESPONSE("Unknown response");

    private final String RESPONSE;

    ResponseMessage(String response){
        this.RESPONSE = response;
    }

    public String getResponse() {
        return RESPONSE;
    }

 /**
  * Converts a string to the corresponding ResponseMessage enum value
  * Iterate over all the values in the ResponseMessage enum
  * Check if the 'response' field of the current enum value matches the input text
  * If a match is found, return the corresponding ResponseMessage enum value
  */
 public static ResponseMessage fromString(String text) {
        log.info("Converting text to OperationResponses: {}", text);
        for (ResponseMessage message : ResponseMessage.values()) {
            if (message.getResponse().equals(text)) {
                log.info("Match found for text: {}", text);
                return message;
            }
        }
        log.warn("No match found for text: {}", text);
        return UNKNOWN_RESPONSE;
    }
}

