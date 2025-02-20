package database;

import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.mindrot.jbcrypt.BCrypt;
import user.credential.User;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.INTEGER;

@Log4j2
public class UserDAO {
    private final DSLContext create;

    public UserDAO(DSLContext create) {
        this.create = create;
        createTable();
    }

    public void createTable(){
        create.createTableIfNotExists("user")
                .column("id", INTEGER.identity(true))
                .column("username", VARCHAR(255).nullable(false))
                .column("password", VARCHAR(255).nullable(false))
                .column("role", VARCHAR(50).nullable(false))
                .column("hashed_password", VARCHAR(255).nullable(false))
                .constraints(
                        DSL.constraint("pk_user").primaryKey("id"),
                        DSL.constraint("uk_user_username").unique("username")
                )
                .execute();
    }

    public void addUserToDB(User user)  {
        create.insertInto(table("user"),
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
        Record record = create.selectFrom("user")
                .where(DSL.field("username").eq(username))
                .fetchOne();

        if (record == null) {
            return null;
        }

        return new User(
                record.getValue("username", String.class),
                record.getValue("password", String.class),
                User.Role.valueOf(record.getValue("role", String.class).toUpperCase())
        );
    }

    public boolean checkPasswordInDB(String typedPassword, String username) {
        Record record = create.selectFrom("user")
                .where(DSL.field("username").eq(username))
                .fetchOne();

        String hashed = record.getValue("hashed_password", String.class);

        return BCrypt.checkpw(typedPassword, hashed);
    }

    public void removeUserFromDB(String username) {
        create.deleteFrom(table("user"))
                .where(field("username").eq(username))
                .execute();
    }

    public void changeUserRoleInDB(User user, User.Role role) {
        updateUserInDB(user);
    }

    public void updateUserInDB(User user) {
        create.update(table("user"))
                .set(field("password"), user.getPassword())
                .set(field("role"), user.getRole().toString())
                .set(field("hashed_password"), user.getHashedPassword())
                .where(field("username").eq(user.getUsername()))
                .execute();
    }
}