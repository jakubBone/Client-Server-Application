package user;

import database.DataBase;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

@Log4j2
public class Admin extends User {
    public final DataBase DATABASE;
    public final DSLContext JOOQ;

    public Admin(DataBase DATABASE, DSLContext JOOQ) {
        super("admin", "java10", Role.ADMIN);
        this.DATABASE = DATABASE;
        this.JOOQ = JOOQ;
        log.info("Admin instance created");
    }

    public void changePassword(User user, String newPassword) {
        log.info("Attempting to password change for user: {}", user.getUsername());
        user.setPassword(newPassword);
        updateDataBase(user);
        log.info("Password change succeeded for user: {}", user.getUsername());
    }

    public void deleteUser(User user) {
        log.info("Attempting to delete user: {}", user.getUsername());
        Table<?> usersTable = DSL.table("users");
        Field<String> usernameField = DSL.field("username", String.class);

        JOOQ.deleteFrom(usersTable)
                        .where(usernameField.eq(user.getUsername()))
                        .execute();
        log.info("User deletion succeeded: {}", user.getUsername());
    }

    public void changeUserRole(User user, User.Role role) {
        log.info("Attempting to role change for user: {}", user.getUsername());
        user.setRole(User.Role.ADMIN);
        updateDataBase(user);
        log.info("Role change succeeded for user: {} to {}", user.getUsername(), role);
    }

    public void switchUser(User user) {
        log.info("Attempting to switch to user: {}", user.getUsername());
        UserManager.currentLoggedInUser = user;
        UserManager.ifAdminSwitched = true;
        log.info("Switched to user: {}", user.getUsername());
    }

    public void updateDataBase(User user) {
        log.info("Attempting to upload database: {}", user.getUsername());
        Table<?> usersTable = DSL.table("users");
        Field<String> usernameField = DSL.field("username", String.class);
        Field<String> passwordField = DSL.field("password", String.class);
        Field<String> roleField = DSL.field("role", String.class);
        Field<String> hashedPasswordField = DSL.field("hashed_password", String.class);

        JOOQ.update(usersTable)
                .set(passwordField, user.getPassword())
                .set(roleField, user.getRole().toString())
                .set(hashedPasswordField, user.getHashedPassword())
                .where(usernameField.eq(user.getUsername()))
                .execute();
        log.info("Data base upload succeeded {}", user.getUsername());
    }
}


