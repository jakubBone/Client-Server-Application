package operations;

public enum OperationResponses {
    LOGIN_SUCCESSFUL_USER("User login successful"),
    LOGIN_SUCCESSFUL_ADMIN("Admin login successful"),
    REGISTRATION_SUCCESSFUL("Registration successful"),
    SUCCESSFULLY_LOGGED_OUT("Successfully logged out"),
    LOGIN_FAILED_INCORRECT_PASSWORD("Login failed: Incorrect password"),
    LOGIN_FAILED_USER_NOT_FOUND("Login failed: User does not exist"),
    REGISTRATION_FAILED_USER_EXISTS("Registration failed: User already exists"),
    OPERATION_SUCCEEDED("Operation succeeded: Authorized"),
    OPERATION_FAILED("Operation failed: Not authorized"),
    SWITCH_SUCCEEDED("Switch operation succeeded: Authorized"),
    SWITCH_FAILED("Switch operation failed: Not authorized"),
    UNKNOWN_RESPONSE("Unknown response");
    private final String RESPONSE;

    OperationResponses(String response){
        this.RESPONSE = response;
    }

    public String getResponse() {
        return RESPONSE;
    }

    public static OperationResponses fromString(String response) {
        for (OperationResponses opResponse : OperationResponses.values()) {
            if (opResponse.getResponse().equals(response)) {
                return opResponse;
            }
        }
        return UNKNOWN_RESPONSE;
    }

}

