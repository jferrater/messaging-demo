package com.github.jferrater.messagingservice.service;

import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
import com.github.jferrater.messagingservice.repository.TextMessageRepository;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class TextMessageService {

    private ModelMapper modelMapper;
    private TextMessageRepository textMessageRepository;

    public TextMessageService(TextMessageRepository textMessageRepository, ModelMapper modelMapper) {
        this.textMessageRepository = textMessageRepository;
        this.modelMapper = modelMapper;
    }

    public TextMessageResponse createMessage(TextMessage textMessage) {
        TextMessageEntity textMessageEntity = toTextMessageEntity(textMessage);
        return toTextMessageResponse(textMessageRepository.insert(textMessageEntity));
    }

    public List<TextMessageResponse> getReceivedMessages(String receiver) {
        List<TextMessageEntity> messagesByReceiver = textMessageRepository.findMessagesByReceiver(receiver);
        return messagesByReceiver.stream().map(this::toTextMessageResponse).collect(toList());
    }

    public TextMessageResponse updateMessage(TextMessageResponse textMessageResponse) {
        TextMessageEntity textMessageEntity = toTextMessageEntity(textMessageResponse);
        TextMessageEntity updated = textMessageRepository.save(textMessageEntity);
        return toTextMessageResponse(updated);
    }

    private TextMessageResponse toTextMessageResponse(TextMessageEntity textMessageEntity) {
        return modelMapper.map(textMessageEntity, TextMessageResponse.class);
    }

    private TextMessageEntity toTextMessageEntity(TextMessage textMessage) {
        String messageId = UUID.randomUUID().toString();
        TextMessageResponse textMessageResponse = new TextMessageResponse(messageId, textMessage.getSender(), textMessage.getReceiver(), textMessage.getMessage(), new Date());
        return modelMapper.map(textMessageResponse, TextMessageEntity.class);
    }

    private TextMessageEntity toTextMessageEntity(TextMessageResponse textMessageResponse) {
        return modelMapper.map(textMessageResponse, TextMessageEntity.class);
    }
}
