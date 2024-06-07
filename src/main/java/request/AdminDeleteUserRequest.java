package request;

public class AdminDeleteUserRequest extends Request {
    public AdminDeleteUserRequest(String updateOperation, String userToUpdate) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
    }
}
