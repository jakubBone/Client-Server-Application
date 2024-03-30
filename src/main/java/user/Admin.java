package user;

import exceptions.UserAuthenticationException;

import java.util.List;


public class Admin extends User {

    public Admin() {
        super("admin", "developer123!!", Role.ADMIN);
    }

    public void requestAccountRemovalByAdmin(String typedUserName, List<User> usersList) throws UserAuthenticationException {
        User userToDelete = null;
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            usersList.remove(userToDelete);
            logger.info("Account delete successful");
        } else {
            throw new UserAuthenticationException("User not found");
        }
    }

    public void requestPasswordChangeByAdmin(String typedUserName, String newPassword, String oldPassword, List<User> usersList) throws UserAuthenticationException {
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                if (ifPasswordEqual(oldPassword, user.getHashedPassword())) {
                    user.setPassword(newPassword);
                    user.setHashedPassword(newPassword.hashCode());
                    logger.info("Password change successful");
                    return;
                } else {
                    throw new UserAuthenticationException("Incorrect old password");
                }
            }
        }
        throw new UserAuthenticationException("User not found");
    }
}

