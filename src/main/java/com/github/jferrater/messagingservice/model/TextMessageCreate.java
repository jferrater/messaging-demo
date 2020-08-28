package com.github.jferrater.messagingservice.model;

import java.util.Date;

public class TextMessageCreate extends TextMessage{

    private String id;
    private Date dateSent;

    public TextMessageCreate() {
        super(null, null, null);
    }

    public TextMessageCreate(String id, String sender, String receiver, String message, Date dateSent) {
        super(sender, receiver, message);
        this.id = id;
        this.dateSent = dateSent;
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
