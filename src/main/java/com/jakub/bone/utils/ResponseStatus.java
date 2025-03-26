package com.jakub.bone.utils;

public enum ResponseStatus {
    OPERATION_SUCCEEDED("operation.succeeded"),
    FAILED_TO_FIND_USER("failed.to.find.user"),
    FAILED_TO_FIND_MESSAGE("failed.to.find.message"),
    REGISTRATION_SUCCESSFUL("registration.successful"),
    REGISTRATION_FAILED_USER_EXISTS("registration.failed.user.exists"),
    USER_LOGIN_SUCCEEDED("user.login.succeeded"),
    ADMIN_LOGIN_SUCCEEDED("admin.login.succeeded"),
    LOGIN_FAILED_INCORRECT_PASSWORD("login.failed.incorrect.password"),
    LOGOUT_SUCCEEDED("logout.succeeded"),
    USER_SWITCH_SUCCEEDED("user.switch.succeeded"),
    ADMIN_SWITCH_SUCCEEDED("admin.switch.succeeded"),
    ROLE_CHANGE_SUCCEEDED("role.change.succeeded"),
    USER_DELETE_SUCCEEDED("user.delete.succeeded"),
    SENDING_SUCCEEDED("sending.succeeded"),
    SENDING_FAILED_BOX_FULL("sending.failed.box.full"),
    MAIL_DELETION_SUCCEEDED("mail.deletion.succeeded"),
    MAILBOX_EMPTY("mailbox.empty"),
    UNKNOWN_REQUEST("unknown.request");

    private final String key;

    ResponseStatus(String key) {
        this.key = key;
    }

    public String getResponse() {
        return ConfigLoader.get(key);
    }
}