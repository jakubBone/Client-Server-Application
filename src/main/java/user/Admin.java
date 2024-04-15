package user;

public class Admin extends User {

    public Admin() {
        super("admin", "a", Role.ADMIN);
    }

    public void changePassword(String username){
        for(User userToRemove: UserManager.usersList){
            if(username.equals(userToRemove.getUsername())){
                UserManager.usersList.remove(userToRemove);
            }
        }
    }

    public void deleteUser(String username){
        for(User userToRemove: UserManager.usersList){
            if(username.equals(userToRemove.getUsername())){
                UserManager.usersList.remove(userToRemove);
            }
        }
    }


}

