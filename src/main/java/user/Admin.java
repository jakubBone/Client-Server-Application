package user;


import java.util.ArrayList;
import java.util.List;

public class Admin extends User {

    List<User> pendingAccountDeletions;

    public Admin() {
        super("admin", "a", Role.ADMIN);
        pendingAccountDeletions = new ArrayList<>();
    }

    public void deleteUser(){
        for(User userToRemove: pendingAccountDeletions){
            UserManager.usersList.remove(userToRemove);
        }
    }

}

