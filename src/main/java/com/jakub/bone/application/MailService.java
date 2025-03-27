package com.jakub.bone.application;

import com.jakub.bone.domain.Mail;
import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.session.SessionManager;
import lombok.extern.log4j.Log4j2;
import lombok.Setter;
import com.jakub.bone.domain.User;
import org.elasticsearch.action.search.SearchResponse;

import java.time.LocalDateTime;
import java.util.List;

import static com.jakub.bone.utils.ResponseStatus.SENDING_FAILED_BOX_FULL;
import static com.jakub.bone.utils.ResponseStatus.SENDING_SUCCEEDED;

@Log4j2
@Setter
public class MailService {
    private final MailRepository mailRepository;
    private final SessionManager sessionManager;
    private final ElasticSearchService searchService;

    public MailService(SessionManager sessionManager, MailRepository mailRepository, ElasticSearchService searchService) {
        this.mailRepository = mailRepository;
        this.sessionManager = sessionManager;
        this.searchService = searchService;
    }

    public String sendMail(User recipient, String message) {
        Mail mail = new Mail(sessionManager.getCurrentUser(), recipient, message, LocalDateTime.now());
        if(mailRepository.isMailboxFull(recipient)){
            return SENDING_FAILED_BOX_FULL.getResponse();
        }
        mailRepository.saveMail(mail);
        log.info("Mail sent successfully to {}", recipient.getUsername());

        // Indeksowanie w Elastic
        try {
            searchService.indexMail(mail);
        } catch (Exception e) {
            log.error("Error indexing mail in Elastic: {}", e.getMessage());
        }
        return SENDING_SUCCEEDED.getResponse();
    }

    public List<Mail> getMails(String boxType) {
        return mailRepository.findMails(boxType, sessionManager);
    }

    public List<Mail> searchMessages(String boxType, String content) {
        return mailRepository.searchText(boxType, sessionManager, content);
    }

    // Wyszukiwanie wiadomości za pomocą Elastic Search
    // Implementacja polega na wywołaniu metody searchMails w ElasticMailIndexer i parsowaniu wyników.
    public SearchResponse searchMessagesInElastic(String query) {
        try {
            return searchService.searchMails(query);
        } catch (Exception e) {
            log.error("Error searching mails in Elastic: {}", e.getMessage());
            return null;
        }
    }

    public void deleteMails(String boxType) {
        List<Mail> mails = mailRepository.findMails(boxType, sessionManager);
        mailRepository.deleteMails(boxType, sessionManager);
        log.info("Deleted {}mails from DB for {}", boxType, sessionManager.getCurrentUser().getUsername());

        // Usuwamy wiadomości z Elastic
        for(Mail mail : mails) {
            // Zakładamy, że mamy unikalny identyfikator wiadomości (mail.getId())
            try {
                searchService.deleteMail(mail.getId().toString());
            } catch (Exception e) {
                log.error("Error deleting mail from Elastic: {}", e.getMessage());
            }
        }
    }
}