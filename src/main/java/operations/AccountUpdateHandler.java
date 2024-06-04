package operations;

import lombok.extern.log4j.Log4j2;
import request.Request;
import server.ServerRequestHandler;
import user.User;
import user.UserManager;

import java.io.IOException;

@Log4j2
public class AccountUpdateHandler {

    // Handles UPDATE request for updating user data. It checks for authorization before proceeding
    public String getUpdateStatus(UserManager userManager) throws IOException {
        String status;
        if (userManager.isCurrentUserAdmin()){
            log.info("Authorized update attempt by admin user");
            ServerRequestHandler.isAuthorized = true;
            status = OperationResponses.OPERATION_SUCCEEDED.getResponse();
        } else {
            status = OperationResponses.OPERATION_FAILED.getResponse();
        }
        return status;
    }

    public String getUpdateResponse(Request reqFromJson, UserManager userManager) throws IOException {
        User userToUpdate = userManager.getUserByUsername(reqFromJson.getUserToUpdate());
        String response = null;
        if(userToUpdate != null ) {
            switch (reqFromJson.getUpdateOperation().toUpperCase()) {
                case "PASSWORD":
                    userManager.changePassword(userToUpdate, reqFromJson.getNewPassword());
                    response = userToUpdate.getUsername() + " password change successful";
                    log.info("Password changed successfully for user: {}", userToUpdate.getUsername());
                    break;
                case "DELETE":
                    if(userToUpdate.getRole().equals(User.Role.ADMIN)){
                        response = "Operation failed: admin account cannot be deleted";
                        log.warn("Attempted to impossible delete admin account for user: {}", userToUpdate.getUsername());
                    } else {
                        userManager.deleteUser(userToUpdate);
                        response = userToUpdate.getUsername() + " account deletion successful";
                        log.info("User account deleted successfully: {}", userToUpdate.getUsername());
                    }
                    break;
            }
        } else {
            response = "Update failed: " + reqFromJson.getUsername() + " not found";
            log.warn("Failed to find user for update: {}", reqFromJson.getUsername());
        }

        return response;
    }
}
