package com.github.jferrater.messagingservice.controller;

import com.github.jferrater.messagingservice.model.MessageIds;
import com.github.jferrater.messagingservice.model.MessageStatus;
import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
import com.github.jferrater.messagingservice.service.TextMessageService;
import com.mongodb.lang.Nullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
public class TextMessageController {

    private TextMessageService textMessageService;

    public TextMessageController(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @PostMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextMessageResponse> sendTextMessage(@RequestBody TextMessage textMessage) {
        TextMessageResponse textMessageResponse = textMessageService.createMessage(textMessage);
        return new ResponseEntity<>(textMessageResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/messages/{username}/received", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getReceivedMessages(
            @PathVariable("username") String username,
            @Nullable @RequestParam @DateTimeFormat(iso = DATE_TIME) Date startDate,
            @Nullable @RequestParam @DateTimeFormat(iso = DATE_TIME) Date stopDate) {
        if(startDate != null && stopDate != null) {
            List<TextMessageResponse> receivedMessagesBetweenDates = textMessageService.getReceivedMessagesBetweenDates(username, startDate, stopDate);
            return new ResponseEntity<>(receivedMessagesBetweenDates, HttpStatus.OK);
        } else {
            List<TextMessageResponse> messages = textMessageService.getReceivedMessages(username);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/messages/{username}/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getNewMessages(
            @PathVariable("username") String username) {
        List<TextMessageResponse> messages = textMessageService.getReceivedMessages(username);
        List<TextMessageResponse> newMessages = messages.stream()
                .filter(message -> MessageStatus.NEW.equals(message.getMessageStatus()))
                .collect(toList());
        List<TextMessageResponse> toBeUpdatedList = new ArrayList<>(newMessages);
        toBeUpdatedList.forEach(m -> {
            m.setMessageStatus(MessageStatus.FETCHED);
            textMessageService.updateMessage(m);
        });
        return new ResponseEntity<>(newMessages, HttpStatus.OK);
    }

    @GetMapping(value = "/messages/{username}/sent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getSentMessages(@PathVariable("username") String username) {
        List<TextMessageResponse> sentMessages = textMessageService.getSentMessages(username);
        return new ResponseEntity<>(sentMessages, HttpStatus.OK);
    }

    @DeleteMapping(value = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@RequestBody MessageIds messageIds) {
        textMessageService.deleteMessages(messageIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/messages/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        textMessageService.deleteMessageById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
