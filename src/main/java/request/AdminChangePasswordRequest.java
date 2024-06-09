package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AdminChangePasswordRequest extends Request {

    public AdminChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        super(updateOperation);
        this.userToUpdate = userToUpdate;
        this.newPassword = newPassword;
        log.info("AdminChangePasswordRequest created for user: {}", userToUpdate);
    }
}
