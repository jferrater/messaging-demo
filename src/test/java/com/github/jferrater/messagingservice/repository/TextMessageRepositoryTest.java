package com.github.jferrater.messagingservice.repository;

import com.github.jferrater.messagingservice.model.MessageStatus;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class TextMessageRepositoryTest {

    @Autowired
    private TextMessageRepository target;

    private static final String MESSAGE_ID = UUID.randomUUID().toString();
    private static final String SENDER = "joffer";
    private static final String RECEIVER = "reihmon";
    private static final String MESSAGE_BODY = "Hello World!";

    @BeforeEach
    void setUp() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageEntity.setMessageStatus(MessageStatus.NEW);
        target.save(textMessageEntity);
    }

    @AfterEach
    void tearDown() {
        target.deleteAll();
    }

    @Test
    void shouldGetMessageById() {
        TextMessageEntity result = target.findById(MESSAGE_ID).orElse(null);

        assertThat(result, is(notNullValue()));
        assertThat(result.getDateSent(), is(notNullValue()));
        assertThat(result.getId(), is(notNullValue()));
        assertThat(result.getSender(), is(SENDER));
        assertThat(result.getReceiver(), is(RECEIVER));
        assertThat(result.getMessage(), is(MESSAGE_BODY));
        assertThat(result.getMessageStatus(), is(MessageStatus.NEW));
    }

    @Test
    void shouldGetMessagesByReceiver() {
        List<TextMessageEntity> result = target.findMessagesByReceiver(RECEIVER);

        assertThat(result.size(), is(1));
    }

    @Test
    void shouldGetSentMessages() {
        List<TextMessageEntity> result = target.findMessagesBySender(SENDER);

        assertThat(result.size(), is(1));
    }
}