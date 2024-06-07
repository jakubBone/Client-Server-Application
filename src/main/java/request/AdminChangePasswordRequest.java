package request;

import static user.User.Role;

public class AdminChangePasswordRequest extends Request {

    // Creates user password change request
    public AdminChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
    }


}
