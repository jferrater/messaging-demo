package com.github.jferrater.messagingservice.repository.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "messages")
public class TextMessageEntity {

    @Id
    private String id;
    private String sender;
    private String receiver;
    private String message;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateSent;

    public TextMessageEntity() {
        // Empty constructor
    }

    public TextMessageEntity(String id, String sender, String receiver, String message, Date dateSent) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.dateSent = dateSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
