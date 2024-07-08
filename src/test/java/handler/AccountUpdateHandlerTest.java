package handler;

import handler.user.PasswordChangeHandler;

class AccountUpdateHandlerTest {
    /*asswordChangeHandler updateHandler;
    UserManager userManager;
    DatabaseConnection DATABASE;
    DSLContext JOOQ;

    @BeforeEach
    void setUp() {
        updateHandler = new PasswordChangeHandler();
        userManager = new UserManager();
        DATABASE = new DatabaseConnection();
        JOOQ = DSL.using(DATABASE.getConnection());
    }

    @Test
    @DisplayName("Should test getting update response for password change")
    void testGetPasswordChangeResponse() {
        Admin admin = new Admin(DATABASE, JOOQ);

        // Admin login
        userManager.login(admin);
        // Password changing
        String response = updateHandler.getChangePasswordResponse(admin.getUsername(), "newPasswod", userManager);

        assertNotEquals(ResponseMessage.AUTHORIZATION_FAILED.getResponse(), response);
        assertNotEquals(ResponseMessage.FAILED_TO_FIND_USER.getResponse(), response);
        assertEquals(ResponseMessage.OPERATION_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test getting update status for user account deletion")
    void testGetUserDeleteResponse() {
        String userName = "exampleUsername";
        String password = "examplePassword";
        Admin admin = new Admin(DATABASE, JOOQ);

        // User register
        userManager.handleRegister(userName, password);
        // Admin login
        userManager.login(admin);
        // User account deletion
        String response = updateHandler.getUserDeleteResponse(userName, userManager);

        assertNotEquals(ResponseMessage.AUTHORIZATION_FAILED.getResponse(), response);
        assertNotEquals(ResponseMessage.FAILED_TO_FIND_USER.getResponse(), response);
        assertEquals(ResponseMessage.OPERATION_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test getting update response for user role change")
    void testGetChangeRoleResponse() {
        Admin admin = new Admin(DATABASE, JOOQ);

        // Admin login
        userManager.login(admin);
        // Role changing
        String response = updateHandler.getChangeRoleResponse(admin.getUsername(), User.Role.USER, userManager);

        assertNotEquals(ResponseMessage.AUTHORIZATION_FAILED.getResponse(), response);
        assertNotEquals(ResponseMessage.FAILED_TO_FIND_USER.getResponse(), response);
        assertEquals(ResponseMessage.ROLE_CHANGE_SUCCEEDED.getResponse(), response);
    }*/
}