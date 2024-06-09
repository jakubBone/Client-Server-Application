package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AdminSwitchUserRequest extends Request{
    public AdminSwitchUserRequest(String requestCommand, String username) {
        super(requestCommand);
        this.userToSwitch = username;
        log.info("AdminSwitchUserRequest created for user: {}", userToSwitch);
    }
}
