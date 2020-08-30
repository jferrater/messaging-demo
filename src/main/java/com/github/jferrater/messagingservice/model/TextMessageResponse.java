package com.github.jferrater.messagingservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

public class TextMessageResponse extends TextMessage{

    @Schema(description = "The id of the text message")
    private String id;
    @Schema(description = "The date the message was sent")
    @DateTimeFormat(iso = DATE_TIME)
    private Date dateSent;
    @Schema(description = "The status of the message")
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
