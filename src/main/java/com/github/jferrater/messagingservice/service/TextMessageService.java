package com.github.jferrater.messagingservice.service;

import com.github.jferrater.messagingservice.exceptions.MessageNotFoundException;
import com.github.jferrater.messagingservice.model.MessageIds;
import com.github.jferrater.messagingservice.model.MessageStatus;
import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
import com.github.jferrater.messagingservice.repository.TextMessageRepository;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

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
        textMessageEntity.setMessageStatus(MessageStatus.NEW);
        return toTextMessageResponse(textMessageRepository.insert(textMessageEntity));
    }

    public List<TextMessageResponse> getReceivedMessages(String receiver) {
        List<TextMessageEntity> messagesByReceiver = textMessageRepository.findMessagesByReceiver(receiver);
        return messagesByReceiver.stream().map(this::toTextMessageResponse).collect(toList());
    }

    public List<TextMessageResponse> getReceivedMessagesBetweenDates(String receiver, Date startIndexDate, Date stopIndexDate) {
        List<TextMessageEntity> messages = textMessageRepository.findMessagesByReceiverAndDateSentBetween(receiver, startIndexDate, stopIndexDate);
        return messages.stream().map(this::toTextMessageResponse).collect(toList());
    }

    public List<TextMessageResponse> getSentMessages(String sender) {
        List<TextMessageEntity> messagesByReceiver = textMessageRepository.findMessagesBySender(sender);
        return messagesByReceiver.stream().map(this::toTextMessageResponse).collect(toList());
    }

    public TextMessageResponse updateMessage(TextMessageResponse textMessageResponse) {
        TextMessageEntity textMessageEntity = toTextMessageEntity(textMessageResponse);
        TextMessageEntity updated = textMessageRepository.save(textMessageEntity);
        return toTextMessageResponse(updated);
    }

    public void deleteMessages(MessageIds messageIds) {
        checkIfMessagesExist(messageIds);
        for(String id : messageIds.getIds()) {
            textMessageRepository.deleteById(id);
        }
    }

    private void checkIfMessagesExist(MessageIds messageIds) {
        for(String id : messageIds.getIds()) {
            Optional<TextMessageEntity> byId = textMessageRepository.findById(id);
            if(byId.isEmpty()){
                throw new MessageNotFoundException(String.format("The text message with an id '%s' is not found!", id));
            }
        }
    }

    public void deleteMessageById(String id) {
        textMessageRepository.deleteById(id);
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
