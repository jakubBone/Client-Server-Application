package request.user;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class UserSwitchRequest extends Request {
    public UserSwitchRequest(String requestCommand, String userToSwitch) {
        setCommand(requestCommand);
        setUserToSwitch(userToSwitch);
    }
}
