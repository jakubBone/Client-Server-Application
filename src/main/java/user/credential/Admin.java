package user.credential;

import lombok.extern.log4j.Log4j2;
import user.credential.User;

@Log4j2
public class Admin extends User {

    public Admin() {
        super("admin", "java10", Role.ADMIN);
        log.info("Admin instance created");
    }

}


