package com.github.jferrater.messagingservice.service;

import com.github.jferrater.messagingservice.repository.TextMessageRepository;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
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
        target = new TextMessageService(textMessageRepository);
    }

    @Test
    void shouldCreateAMessage() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        when(textMessageRepository.insert(eq(textMessageEntity))).thenReturn(textMessageEntity);

        TextMessageEntity result = target.createMessage(textMessageEntity);

        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(MESSAGE_ID));
        assertThat(result.getSender(), is(SENDER));
        assertThat(result.getReceiver(), is(RECEIVER));
        assertThat(result.getMessage(), is(MESSAGE_BODY));
        assertThat(result.getDateSent(), is(notNullValue()));
    }
}