package user;

import exceptions.UserAuthenticationException;

import java.util.List;


public class Admin extends User {

    public Admin() {
        super("admin", "developer123!!", Role.ADMIN);
    }


}

