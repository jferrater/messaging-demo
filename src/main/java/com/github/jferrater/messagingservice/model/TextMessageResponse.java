package com.github.jferrater.messagingservice.model;

import java.util.Date;

public class TextMessageResponse extends TextMessage{

    private String id;
    private Date dateSent;
    private MessageStatus messageStatus;

    public TextMessageResponse() {
        super(null, null, null);
    }

    public TextMessageResponse(String id, String sender, String receiver, String message, Date dateSent) {
        super(sender, receiver, message);
        this.id = id;
        this.dateSent = dateSent;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

}
