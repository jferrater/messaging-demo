package com.github.jferrater.messagingservice.service;

import com.github.jferrater.messagingservice.model.MessageStatus;
import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
import com.github.jferrater.messagingservice.repository.TextMessageRepository;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TextMessageServiceTest {

    private TextMessageService target;
    private TextMessageRepository textMessageRepository;

    private static final String MESSAGE_ID = "b2b1f340-cba2-11e8-ad5d-873445c542a2";
    private static final String SENDER = "joffer";
    private static final String RECEIVER = "reihmon";
    private static final String MESSAGE_BODY = "Hello World!";

    @BeforeEach
    void setUp() {
        textMessageRepository = mock(TextMessageRepository.class);
        target = new TextMessageService(textMessageRepository, new ModelMapper());
    }

    @Test
    void shouldCreateAMessage() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        when(textMessageRepository.insert(any(TextMessageEntity.class))).thenReturn(textMessageEntity);
        TextMessage textMessage = new TextMessage(SENDER,RECEIVER, MESSAGE_BODY);

        TextMessageResponse result = target.createMessage(textMessage);

        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(MESSAGE_ID));
        assertThat(result.getSender(), is(SENDER));
        assertThat(result.getReceiver(), is(RECEIVER));
        assertThat(result.getMessage(), is(MESSAGE_BODY));
        assertThat(result.getDateSent(), is(notNullValue()));
    }

    @Test
    void shouldGetReceivedMessages() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageEntity.setMessageStatus(MessageStatus.NEW);
        when(textMessageRepository.findMessagesByReceiver(anyString())).thenReturn(List.of(textMessageEntity));

        List<TextMessageResponse> result = target.getReceivedMessages(RECEIVER);

        assertThat(result.size(), is(1));
    }

    @Test
    void shouldUpdateMessage() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageEntity.setMessageStatus(MessageStatus.FETCHED);
        when(textMessageRepository.save(any(TextMessageEntity.class))).thenReturn(textMessageEntity);
        TextMessageResponse textMessageResponse = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageResponse.setMessageStatus(MessageStatus.NEW);

        TextMessageResponse result = target.updateMessage(textMessageResponse);

        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(MESSAGE_ID));
        assertThat(result.getSender(), is(SENDER));
        assertThat(result.getReceiver(), is(RECEIVER));
        assertThat(result.getMessage(), is(MESSAGE_BODY));
        assertThat(result.getMessageStatus(), is(MessageStatus.FETCHED));
        assertThat(result.getDateSent(), is(notNullValue()));
    }

    @Test
    void shouldGetSentMessages() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageEntity.setMessageStatus(MessageStatus.NEW);
        when(textMessageRepository.findMessagesBySender(anyString())).thenReturn(List.of(textMessageEntity));

        List<TextMessageResponse> result = target.getSentMessages(SENDER);

        assertThat(result.size(), is(1));
    }

}