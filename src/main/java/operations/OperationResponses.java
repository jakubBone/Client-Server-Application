package operations;

public enum OperationResponses {
    LOGIN_SUCCESSFUL("Login successful"),
    REGISTRATION_SUCCESSFUL("Registration successful"),
    SUCCESSFULLY_LOGGED_OUT("Successfully logged out"),
    LOGIN_FAILED("Login failed: Incorrect username or password"),
    REGISTRATION_FAILED("Registration failed"),
    OPERATION_SUCCEEDED("Operation succeeded: Authorized"),
    OPERATION_FAILED("Operation failed: Not authorized");

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
        return null;
    }

}

