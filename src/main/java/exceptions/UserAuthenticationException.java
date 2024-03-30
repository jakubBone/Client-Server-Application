package exceptions;

public class UserAuthenticationException extends Exception {

    public UserAuthenticationException(String message){
        super(message);
    }

    public void handleException() {
        if (this.getMessage().equals("Login failed")) {
            System.err.println("Login error: Incorrect username or password");
        } else if (this.getMessage().equals("Registration failed")) {
            System.err.println("Registration error: Failed to register user");
        } else if (this.getMessage().equals("User not found")) {
            System.err.println("User not found: The specified user does not exist");
        } else {
            System.err.println("Unidentified error: " + this.getMessage());
        }
    }
}
