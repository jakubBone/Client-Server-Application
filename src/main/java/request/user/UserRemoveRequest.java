package request;
import lombok.extern.log4j.Log4j2;
@Log4j2
public class UserRemoveRequest extends Request {
    public UserRemoveRequest(String updateOperation, String userToDelete) {
        setCommand(updateOperation);
        setUserToUpdate(userToDelete);
        log.info("Remove request created for user: {}", userToDelete);
    }
}
