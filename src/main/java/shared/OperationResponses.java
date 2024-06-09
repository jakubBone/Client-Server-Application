package shared;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum OperationResponses {

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

    // Unknown Response
    UNKNOWN_RESPONSE("Unknown response");

    private final String RESPONSE;

    OperationResponses(String response){
        this.RESPONSE = response;
    }

    public String getResponse() {
        return RESPONSE;
    }

    public static OperationResponses fromString(String text) {
        log.info("Converting text to OperationResponses: {}", text);
        for (OperationResponses opResponse : OperationResponses.values()) {
            if (opResponse.getResponse().equals(text)) {
                log.info("Match found for text: {}", text);
                return opResponse;
            }
        }
        log.warn("No match found for text: {}", text);
        return UNKNOWN_RESPONSE;
    }
}

