package com.github.jferrater.messagingservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class TextMessage {

    @Schema(description = "The username of the sender")
    @NotNull
    private String sender;
    @Schema(description = "The username of the receiver")
    @NotNull
    private String receiver;
    @Schema(description = "The content of the text message")
    @NotNull
    private String message;

    public TextMessage(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
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
}
