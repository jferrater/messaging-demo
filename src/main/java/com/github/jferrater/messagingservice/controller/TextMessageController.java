package com.github.jferrater.messagingservice.controller;

import com.github.jferrater.messagingservice.model.MessageStatus;
import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
import com.github.jferrater.messagingservice.service.TextMessageService;
import com.mongodb.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class TextMessageController {

    private TextMessageService textMessageService;

    public TextMessageController(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @PostMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextMessageResponse> sendTextMessage(@RequestBody TextMessage textMessage) {
        TextMessageResponse textMessageResponse = textMessageService.createMessage(textMessage);
        return new ResponseEntity<>(textMessageResponse, HttpStatus.CREATED);
    }

    @GetMapping(value = "/messages/{username}/received", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getReceivedMessages(
            @PathVariable("username") String username,
            @Nullable @RequestParam boolean only_new_messages) {
        List<TextMessageResponse> messages = textMessageService.getReceivedMessages(username);
        if (only_new_messages) {
            List<TextMessageResponse> newMessages = messages.stream()
                    .filter(message -> MessageStatus.NEW.equals(message.getMessageStatus()))
                    .collect(toList());
            List<TextMessageResponse> toBeUpdatedList = new ArrayList<>(newMessages);
            toBeUpdatedList.forEach(m -> {
                m.setMessageStatus(MessageStatus.FETCHED);
                textMessageService.updateMessage(m);
            });
            return new ResponseEntity<>(newMessages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }
}
