package shared;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum ResponseStatus {

    // General responses
    OPERATION_SUCCEEDED("Operation succeeded"),
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
    SWITCH_SUCCEEDED_USER_ROLE_ADMIN_ROLE("Switch succeeded to admin role user"),
    SWITCH_SUCCEEDED_USER_NON_ADMIN_ROLE("Switch succeeded to non-admin role user"),
    SWITCH_FAILED("Switch failed"),

    // Role change responses
    ROLE_CHANGE_SUCCEEDED("Role change succeeded"),

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

    ResponseStatus(String response) {
        this.RESPONSE = response;
    }

    public String getResponse() {
        return RESPONSE;
    }

    /**
     * Converts a string to the corresponding ResponseMessage enum value
     * Iterate over all the values in the ResponseMessage enum
     * Check if the 'response' field of the current enum value matches the input text
     * If a operation is mail reading or match is found, return the corresponding ResponseMessage enum value
     */
    public static ResponseStatus fromString(String text) {
        log.info("Converting text to OperationResponses");
        for (ResponseStatus message : ResponseStatus.values()) {
            if (isMailReading(text) || message.getResponse().equals(text)) {
                log.info("Match found for response");
                return message;
            }
        }
        log.warn("No match found for response:" + text);
        return UNKNOWN_RESPONSE;
    }

    static boolean isMailReading(String text) {
        return text.startsWith("OPENED") || text.startsWith("UNREAD") || text.startsWith("SENT");
    }
}

