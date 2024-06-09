package request;

import user.User;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AdminChangeRoleRequest extends Request{
    public AdminChangeRoleRequest(String updateOperation, String userToUpdate, User.Role role) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
        this.newRole = role;
        log.info("AdminChangeRoleRequest created for user: {} with new role: {}", userToUpdate, newRole);
    }
}
