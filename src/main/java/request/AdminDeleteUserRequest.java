package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AdminDeleteUserRequest extends Request {
    public AdminDeleteUserRequest(String updateOperation, String userToDelete) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
        log.info("AdminDeleteUserRequest created for user: {}", userToDelete);
    }
}
