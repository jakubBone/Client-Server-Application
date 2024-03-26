package utils;

public class Admin extends User{
    public Admin(String username, String hashedPassword) {
        super("jakubBone", Integer.toString("developer123".hashCode()), Role.ADMIN);
    }
}
