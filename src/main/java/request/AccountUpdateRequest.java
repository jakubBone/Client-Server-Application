package request;

public class AccountUpdateRequest extends Request {

    // Creates user password change request
    public AccountUpdateRequest(String request, String updateOperation, String userToUpdate, String newPassword) {
        super(request);
        this.updateOperation = updateOperation;
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
    }

    // Creates user account deletion request
    public AccountUpdateRequest(String request, String updateOperation, String userToUpdate) {
        super(request);
        this.updateOperation = updateOperation;
        this.userToUpdate = userToUpdate;
    }
}
