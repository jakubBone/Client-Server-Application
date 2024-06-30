package handler;

import lombok.Getter;
import lombok.Setter;

import handler.user.PasswordChangeHandler;
import handler.user.RoleChangeHandler;
import handler.user.UserDeleteHandler;
import handler.user.UserSwitchHandler;
import handler.auth.AuthHandler;
import handler.auth.LogoutHandler;
import handler.mail.MailboxHandler;
import handler.mail.WriteHandler;
import handler.server.ServerDetailsHandler;
@Getter
@Setter
public class HandlerFactory {
    private AuthHandler authHandler;
    private ServerDetailsHandler serverInfoHandler;
    private MailboxHandler mailHandler;
    private WriteHandler writeHandler;
    private LogoutHandler logoutHandler;
    private PasswordChangeHandler passwordHandler;
    private RoleChangeHandler roleHandler;
    private UserDeleteHandler deleteHandler;
    private UserSwitchHandler switchHandler;

    public HandlerFactory(){
        this.authHandler = new AuthHandler();
        this.serverInfoHandler = new ServerDetailsHandler();
        this.mailHandler = new MailboxHandler();
        this.writeHandler = new WriteHandler();
        this.passwordHandler = new PasswordChangeHandler();
        this.roleHandler = new RoleChangeHandler();
        this.deleteHandler = new UserDeleteHandler();
        this.switchHandler = new UserSwitchHandler();
        this.logoutHandler = new LogoutHandler();
    }
}
