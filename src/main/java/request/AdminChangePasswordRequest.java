package request;

public class AdminChangePasswordRequest extends Request {

    public AdminChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
    }
}
