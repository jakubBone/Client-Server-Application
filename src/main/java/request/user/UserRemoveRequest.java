package request.user;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class UserRemoveRequest extends Request {
    public UserRemoveRequest(String updateOperation, String userToDelete) {
        setCommand(updateOperation);
        setUserToUpdate(userToDelete);
    }
}
