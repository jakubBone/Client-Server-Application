package request.user;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class UserChangePasswordRequest extends Request {
    public UserChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        setCommand(updateOperation);
        setUserToUpdate(userToUpdate);
        setNewPassword(newPassword);
        log.info("Password change request created for user: {}", userToUpdate);
    }
}
