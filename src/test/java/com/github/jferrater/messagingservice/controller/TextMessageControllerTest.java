package com.github.jferrater.messagingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.repository.TextMessageRepository;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import com.github.jferrater.messagingservice.service.TextMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class TextMessageControllerTest {

    private static final String MESSAGE_ID = "b2b1f340-cba2-11e8-ad5d-873445c542a2";
    private static final String SENDER = "joffer";
    private static final String RECEIVER = "reihmon";
    private static final String MESSAGE_BODY = "Hello World!";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TextMessageService textMessageService;

    @Test
    void shouldSendAMessage() throws Exception {
        TextMessageEntity textMessageEntity = new TextMessageEntity(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        when(textMessageService.createMessage(any(TextMessageEntity.class))).thenReturn(textMessageEntity);

        String requestBody = requestBody();
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(MESSAGE_ID)))
                .andExpect(jsonPath("$.sender", is(SENDER)))
                .andExpect(jsonPath("$.receiver", is(RECEIVER)))
                .andExpect(jsonPath("$.message", is(MESSAGE_BODY)))
                .andExpect(jsonPath("$.dateSent", is(notNullValue())));
    }

    private static String requestBody() throws JsonProcessingException {
        TextMessage textMessage = new TextMessage(SENDER, RECEIVER, MESSAGE_BODY);
        return new ObjectMapper().writeValueAsString(textMessage);
    }
}