package user;

import utils.JsonConverter;

public class Admin extends User {

    JsonConverter jsonConverter = new JsonConverter();

    public Admin() {
        super("admin", "java10", Role.ADMIN);
    }

    public void changePassword(User user, String newPassword){
        user.setPassword(newPassword);
        user.hashPassword();
        jsonConverter.saveUserData(user);
    }

    public void deleteUser(User user){
        UserManager.usersList.remove(user);
    }

}


