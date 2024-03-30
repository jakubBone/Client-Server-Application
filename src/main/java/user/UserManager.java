package user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    private List<User> usersList;
    private List <String> passwordChangeRequesters;
    private List <String> removeAccountRequesters;

    public UserManager() {
        this.usersList = new ArrayList<>();
        this.passwordChangeRequesters = new ArrayList<>();
        this.removeAccountRequesters = new ArrayList<>();
    }
}
