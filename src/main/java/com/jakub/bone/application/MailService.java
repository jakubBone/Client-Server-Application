package com.jakub.bone.application;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.session.SessionManager;
import com.jakub.bone.utils.ResponseStatus;
import lombok.extern.log4j.Log4j2;
import lombok.Setter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.jakub.bone.domain.User;

import java.util.List;

@Log4j2
@Setter
public class MailService {
    private final DSLContext context;
    private MailRepository mailRepository;
    private UserRepository userRepository;

    public MailService() {
        this.context = DSL.using(DataSource.getInstance().getConnection());
        this.userRepository = new UserRepository(context);
        this.mailRepository = new MailRepository(context, userRepository);
    }

    public String sendMail(User recipient, String message) {
        Mail mail = new Mail(SessionManager.getInstance().getCurrentUser(), recipient, message, Mail.Status.SENT);
        mailRepository.createMail(mail);

        Mail mailToRecipient = new Mail(mail.getSender(), recipient, message, Mail.Status.UNREAD);
        mailRepository.createMail(mailToRecipient);

        log.info("Mail successfully sent to {}", recipient.getUsername());
        return ResponseStatus.SENDING_SUCCEEDED.getResponse();
    }

    public List<Mail> getMails(String boxType) {
        return mailRepository.findMails(boxType);
    }

    public boolean isMailboxFull(User recipient){
        return mailRepository.isMailboxFull(recipient);
    }


    public void deleteMails(String boxType) {
        log.info("Deleting mails from box: {}", boxType);

        mailRepository.deleteMails(boxType);

        //log.info("{} mails deleted for user {}", boxType, UserService.currentLoggedInUser.getUsername());
        log.info("{} mails deleted for user {}", boxType, SessionManager.getInstance().getCurrentUser());
    }

    public void markAsRead() {
        log.info("Marking mails as read");

        mailRepository.markAsReadInDB();

        //log.info("Marked all unread mails as opened for user {}", UserService.currentLoggedInUser.getUsername());
        log.info("Marked all unread mails as opened for user {}", SessionManager.getInstance().getCurrentUser());
    }
}