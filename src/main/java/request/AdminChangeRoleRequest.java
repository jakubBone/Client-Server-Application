package request;

import user.User;

public class AdminChangeRoleRequest extends Request{

    public AdminChangeRoleRequest(String updateOperation, String userToUpdate, User.Role role) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
        this.newRole = role;
    }
}
