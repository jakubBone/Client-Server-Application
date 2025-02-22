package com.jakub.bone.application.service;

import com.jakub.bone.database.DataSource;
import com.jakub.bone.domain.model.Mail;
import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import lombok.Setter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.jakub.bone.domain.model.User;

import java.util.List;

@Log4j2
@Setter
public class MailService {
    private final DSLContext create;
    private MailRepository mailDAO;
    private UserRepository userDAO;

    public MailService() {
        this.create = DSL.using(DataSource.getInstance().getConnection());
        this.userDAO = new UserRepository(create);
        this.mailDAO = new MailRepository(create, userDAO);
    }

    public void sendMail(User recipient, String message) {
        log.info("Mail sending to {} from {}", recipient, UserService.currentLoggedInUser);

        Mail mailToSender = new Mail(UserService.currentLoggedInUser, recipient, message, Mail.Status.SENT);
        mailDAO.saveMailToDB(mailToSender);

        Mail mailToRecipient = new Mail(mailToSender.getSender(), recipient, message, Mail.Status.UNREAD);
        mailDAO.saveMailToDB(mailToRecipient);


        log.info("Mail successfully sent to {}", recipient.getUsername());
    }

    public List<Mail> getMails(String boxType) {
        return mailDAO.getMailsFromDB(boxType);
    }

    public boolean isMailboxFull(User recipient){
        return mailDAO.isMailboxFullInDB(recipient);
    }


    public void deleteMails(String boxType) {
        log.info("Deleting mails from box: {}", boxType);

        mailDAO.deleteMailsFromDB(boxType);

        log.info("{} mails deleted for user {}", boxType, UserService.currentLoggedInUser.getUsername());
    }

    public void markAsRead() {
        log.info("Marking mails as read");

        mailDAO.markAsReadInDB();

        log.info("Marked all unread mails as opened for user {}", UserService.currentLoggedInUser.getUsername());
    }
}