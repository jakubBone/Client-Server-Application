package request.user;
import lombok.extern.log4j.Log4j2;
import request.Request;
import user.credential.User;

@Log4j2
public class UserChangeRoleRequest extends Request {
    public UserChangeRoleRequest(String updateOperation, String userToUpdate, User.Role role) {
        setCommand(updateOperation);
        setUserToUpdate(userToUpdate);
        setNewRole(role);
        log.info("Role change request created for user: {} with new role: {}", userToUpdate, role);
    }
}
