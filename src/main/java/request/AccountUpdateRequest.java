package request;

public class AccountUpdateRequest extends Request {

    // Creates user password change request
    public AccountUpdateRequest(String requestCommand, String updateOperation, String userToUpdate, String newPassword) {
        super(requestCommand);
        this.updateOperation = updateOperation;
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
    }

    // Creates user account deletion request
    public AccountUpdateRequest(String requestCommand, String updateOperation, String userToUpdate) {
        super(requestCommand);
        this.updateOperation = updateOperation;
        this.userToUpdate = userToUpdate;
    }
}
