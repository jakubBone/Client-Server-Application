package user;

import client.Client;

public class Admin extends User {

    public Admin() {
        super("admin", "a", Role.ADMIN);
    }

    public void changePassword(User user, String newPassword){
            user.setPassword(newPassword);
    }

    public void deleteUser(User user){
        UserManager.usersList.remove(user);

    }
}


