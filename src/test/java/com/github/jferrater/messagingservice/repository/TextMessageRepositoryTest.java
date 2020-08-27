package com.github.jferrater.messagingservice.repository;

import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class TextMessageRepositoryTest {

    @Autowired
    private TextMessageRepository target;

    private static final String MESSAGE_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, "joffer", "reihmon", "Hello, Reihmon!", new Date());
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
    }
}