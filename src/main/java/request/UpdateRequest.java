package request;

public class UpdateRequest extends Request{
    public UpdateRequest(String updateOperation, String userToUpdate, String newPassword) {
        this.updateOperation = updateOperation;
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
    }

    public UpdateRequest(String request) {
        this.requestCommand = request;
    }
}
