package com.github.jferrater.messagingservice.controller;

import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
import com.github.jferrater.messagingservice.service.TextMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<TextMessageResponse>> getReceivedMessages(@PathVariable("username") String username){
        List<TextMessageResponse> messages = textMessageService.getReceivedMessages(username);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
