package database;

import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.mindrot.jbcrypt.BCrypt;
import user.User;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Log4j2
public class UserDAO {
    private final DSLContext create;
    private final String USERS_TABLE = "users";

    public UserDAO(DSLContext create) {
        this.create = create;
    }

    public void addUser(User user)  {
        create.insertInto(table(USERS_TABLE),
                        field("username"),
                        field("password"),
                        field("role"),
                        field("hashed_password"))
                .values(user.getUsername(),
                        user.getPassword(),
                        user.getRole().toString(),
                        user.getHashedPassword())
                .execute();
    }

    public User getUserFromDB(String username) {
        Record record = create.selectFrom(USERS_TABLE)
                .where(DSL.field("username").eq(username))
                .fetchOne();

        return new User(
                record.getValue("username", String.class),
                record.getValue("password", String.class),
                User.Role.valueOf(record.getValue("role", String.class).toUpperCase())
        );
    }

    public boolean checkPasswordInDB(String typedPassword, String username) {
        Record record = create.selectFrom("users")
                .where(DSL.field("username").eq(username))
                .fetchOne();

        String hashed = record.getValue("hashed_password", String.class);

        return BCrypt.checkpw(typedPassword, hashed);
    }
    public void deleteUser(String username) {
        log.info("Attempting to delete user: {}", username);

        create.deleteFrom(table(USERS_TABLE))
                .where(field("username").eq(username))
                .execute();

        log.info("User deletion succeeded: {}", username);
    }

    public void changeUserRole(User user, User.Role role) {
        log.info("Attempting to role change for user: {}", user.getUsername());

        user.setRole(role);
        updateUserInDB(user);

        log.info("Role change succeeded for user: {} to {}", user.getUsername(), role);
    }

    public void updateUserInDB(User user) {
        log.info("Attempting to upload database: {}", user.getUsername());

        create.update(table(USERS_TABLE))
                .set(field("password"), user.getPassword())
                .set(field("role"), user.getRole().toString())
                .set(field("hashed_password"), user.getHashedPassword())
                .where(field("username").eq(user.getUsername()))
                .execute();

        log.info("Data base upload succeeded {}", user.getUsername());
    }
}