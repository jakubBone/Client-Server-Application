package com.jakub.bone.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Mail {
    private User sender;
    private User recipient;
    private String message;
    private LocalDateTime sendTime;

    public Mail(User sender, User recipient, String message, LocalDateTime sendTime) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.sendTime = sendTime;
    }
}
