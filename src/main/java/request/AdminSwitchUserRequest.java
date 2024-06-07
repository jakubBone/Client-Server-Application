package request;
public class AdminSwitchUserRequest extends Request{
    public AdminSwitchUserRequest(String requestCommand, String username) {
        super(requestCommand);
        this.userToSwitch = username;
    }
}
