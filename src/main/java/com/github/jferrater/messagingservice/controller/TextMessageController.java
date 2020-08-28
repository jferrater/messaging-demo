package com.github.jferrater.messagingservice.controller;

import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageCreate;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import com.github.jferrater.messagingservice.service.TextMessageService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class TextMessageController {

    private ModelMapper modelMapper;
    private TextMessageService textMessageService;

    public TextMessageController(TextMessageService textMessageService, ModelMapper modelMapper) {
        this.textMessageService = textMessageService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextMessageCreate> sendTextMessage(@RequestBody TextMessage textMessage) {
        TextMessageEntity textMessageEntity = toTextMessageEntity(textMessage);
        TextMessageCreate textMessageCreate = toTextMessageResponse(textMessageService.createMessage(textMessageEntity));
        return new ResponseEntity<>(textMessageCreate, HttpStatus.CREATED);
    }

    private TextMessageCreate toTextMessageResponse(TextMessageEntity textMessageEntity) {
        return modelMapper.map(textMessageEntity, TextMessageCreate.class);
    }

    private TextMessageEntity toTextMessageEntity(TextMessage textMessage) {
        String messageId = UUID.randomUUID().toString();
        TextMessageCreate textMessageCreate = new TextMessageCreate(messageId, textMessage.getSender(), textMessage.getReceiver(), textMessage.getMessage(), new Date());
        return modelMapper.map(textMessageCreate, TextMessageEntity.class);
    }
}
