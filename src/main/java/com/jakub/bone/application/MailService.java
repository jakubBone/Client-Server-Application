package com.jakub.bone.application;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.session.SessionManager;
import lombok.extern.log4j.Log4j2;
import lombok.Setter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.jakub.bone.domain.User;

import java.time.LocalDateTime;
import java.util.List;

import static com.jakub.bone.utils.ResponseStatus.SENDING_FAILED_BOX_FULL;
import static com.jakub.bone.utils.ResponseStatus.SENDING_SUCCEEDED;

@Log4j2
@Setter
public class MailService {
    private final DSLContext context;
    private MailRepository mailRepository;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    public MailService(SessionManager sessionManager) {
        this.context = DSL.using(DataSource.getInstance().getConnection());
        this.userRepository = new UserRepository(context);
        this.mailRepository = new MailRepository(context, userRepository);
        this.sessionManager = sessionManager;
    }

    public String sendMail(User recipient, String message) {
        Mail mail = new Mail(sessionManager.getCurrentUser(), recipient, message, LocalDateTime.now());

        if(mailRepository.isMailboxFull(recipient)){
            return SENDING_FAILED_BOX_FULL.getResponse();
        }

        mailRepository.saveMail(mail);
        log.info("Mail sent successfully to {}", recipient.getUsername());
        return SENDING_SUCCEEDED.getResponse();
    }

    public List<Mail> getMails(String boxType) {
        return mailRepository.findMails(boxType, sessionManager);
    }

    public void deleteMails(String boxType) {
        mailRepository.deleteMails(boxType, sessionManager);
        log.info("Soft-deleted {} emails for user {}", boxType, sessionManager.getCurrentUser().getUsername());
    }
}