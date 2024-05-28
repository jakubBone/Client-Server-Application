package request;

public class AccountUpdateRequest extends Request{
    public AccountUpdateRequest(String updateOperation, String userToUpdate, String newPassword) {
        this.updateOperation = updateOperation;
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
    }
    public AccountUpdateRequest(String request) {
        super(request);
    }

}
