package com.jakub.bone.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.domain.User;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElasticSearchHandler implements CommandHandler{
    private final MailService mailService;

    public ElasticSearchHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String query = commandDTO.getPayload().get("messageContent");

        SearchResponse response = mailService.searchMessagesInElastic(query);

        List<Mail> mails = mapSearchResponseToMails(response);
        StringBuilder responseBuilder = new StringBuilder("\nSEARCH RESULTS: \n\n");
        for (Mail mail : mails) {
                responseBuilder.append("To: ").append(mail.getRecipient().getUsername()).append("\n");
                responseBuilder.append("From: ").append(mail.getSender().getUsername()).append("\n");
            responseBuilder.append(" Message: ").append(mail.getMessage()).append("\n\n");
        }
        return responseBuilder.toString();
    }

    /* @Override
    public String execute(CommandDTO commandDTO) {
        String query = commandDTO.getPayload().get("messageContent");

        SearchResponse response = mailService.searchMessagesInElastic(query);
        StringBuilder sb = new StringBuilder("Search Results:\n");
        for (SearchHit hit : response.getHits().getHits()) {
            sb.append(hit.getSourceAsString()).append("\n");
        }
        return sb.toString();
    }*/

    public List<Mail> mapSearchResponseToMails(SearchResponse response) {
        List<Mail> mails = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();
            String senderUsername = (String) source.get("sender");
            String recipientUsername = (String) source.get("recipient");
            String message = (String) source.get("message");
            String sendTimeStr = (String) source.get("sendTime");
            LocalDateTime sendTime = LocalDateTime.parse(sendTimeStr);

            // Tworzymy uproszczone obiekty User - ewentualnie można pobrać pełne dane z bazy
            User sender = new User(senderUsername, "dummy", User.Role.USER);
            User recipient = new User(recipientUsername, "dummy", User.Role.USER);

            Mail mail = new Mail(sender, recipient, message, sendTime);
            mails.add(mail);
        }
        return mails;
    }
}
