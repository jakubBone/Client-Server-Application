package com.jakub.bone.repository;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.domain.Admin;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.mindrot.jbcrypt.BCrypt;
import com.jakub.bone.domain.User;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.INTEGER;

@Log4j2
public class UserRepository {
    private final DSLContext context;

    public UserRepository() {
        this.context = DSL.using(DataSource.getInstance().getConnection());
        createTable();
        initAdmin();
    }

    private void initAdmin(){
        User admin = findUserByUsername("admin");
        if(admin == null){
            createUser(new Admin());
        }
    }

    public void createTable() {
        try {
            context.createTableIfNotExists("user")
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
        } catch (Exception e) {
            log.error("Error while 'user' table creating: {}", e.getMessage());
            throw new RuntimeException("Failed to create 'user' table ", e);
        }
    }

    public void createUser(User user) {
        try {
            context.insertInto(table("user"),
                            field("username"),
                            field("password"),
                            field("role"),
                            field("hashed_password"))
                    .values(user.getUsername(),
                            user.getPassword(),
                            user.getRole().toString(),
                            user.getHashedPassword())
                    .execute();
        } catch (Exception e) {
            log.error("Error while creating user {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Failed to create user " + user.getUsername(), e);
        }
    }
    public void truncateTable(){
        try {
            context.truncate("user").restartIdentity().execute();
        } catch (Exception e) {
            log.error("Error while table truncating: {}", e.getMessage());
            throw new RuntimeException("Failed to truncate 'mail' table ", e);
        }
    }

    public User findUserByUsername(String username) {
        try {
            Record record = context.selectFrom("user")
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
        } catch (Exception e) {
            log.error("Error while finding user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to find user " + username, e);
        }
    }

    public boolean verifyUserPassword(String typedPassword, String username) {
        try {
            Record record = context.selectFrom("user")
                    .where(DSL.field("username").eq(username))
                    .fetchOne();

            String hashed = record.getValue("hashed_password", String.class);

            return BCrypt.checkpw(typedPassword, hashed);
        } catch (Exception e) {
            log.error("Error while verifying password for user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to verify password for user " + username, e);
        }

    }

    public void removeUser(String username) {
        try {
            context.deleteFrom(table("user"))
                    .where(field("username").eq(username))
                    .execute();
        } catch (Exception e) {
            log.error("Error while deleting user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to delete user " + username, e);
        }
    }

    public void changeUserRole(User user, User.Role role) {
        updateUser(user);
    }

    public void updateUser(User user) {
        try {
            context.update(table("user"))
                    .set(field("password"), user.getPassword())
                    .set(field("role"), user.getRole().toString())
                    .set(field("hashed_password"), user.getHashedPassword())
                    .where(field("username").eq(user.getUsername()))
                    .execute();
        } catch (Exception e) {
            log.error("Error while updating user {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Failed to update user " + user.getUsername(), e);
        }
    }
}