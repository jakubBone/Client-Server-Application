package user;

public class Admin extends User {

    public Admin() {
        super("admin", "a", Role.ADMIN);
    }

    public void changePassword(User user, String newPassword){
            user.setPassword(newPassword);
                System.out.println("new1: " + user.getPassword());
    }

    public void deleteUser(User user){
        UserManager.usersList.remove(user);
    }
}


