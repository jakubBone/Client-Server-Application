package utils;

public class Admin extends User{
    public Admin(String username, String hashedPassword) {
        super("jakubBone", "developer123", Role.ADMIN);
    }
}
