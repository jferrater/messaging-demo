package com.github.jferrater.messagingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.messagingservice.model.MessageIds;
import com.github.jferrater.messagingservice.model.MessageStatus;
import com.github.jferrater.messagingservice.model.TextMessage;
import com.github.jferrater.messagingservice.model.TextMessageResponse;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        TextMessageResponse textMessageResponse = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        when(textMessageService.createMessage(any(TextMessage.class))).thenReturn(textMessageResponse);

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

    @Test
    void shouldGetReceivedMessages() throws Exception {
        TextMessageResponse textMessageResponse = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        when(textMessageService.getReceivedMessages(RECEIVER)).thenReturn(List.of(textMessageResponse));

        mockMvc.perform(get("/messages/"+RECEIVER+"/received")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(MESSAGE_ID)))
                .andExpect(jsonPath("$[0].sender", is(SENDER)))
                .andExpect(jsonPath("$[0].receiver", is(RECEIVER)))
                .andExpect(jsonPath("$[0].message", is(MESSAGE_BODY)))
                .andExpect(jsonPath("$[0].dateSent", is(notNullValue())));
    }

    @Test
    void shouldGetReceivedMessagesBetweenDates() throws Exception {
        TextMessageResponse textMessageResponse = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        when(textMessageService.getReceivedMessagesBetweenDates(anyString(), any(Date.class), any(Date.class))).thenReturn(List.of(textMessageResponse));

        mockMvc.perform(get("/messages/"+RECEIVER+"/received?&startDate=2020-08-29T15:00:10.929Z&stopDate=2020-08-29T17:30:15.622Z")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldOnlyGetNewMessages() throws Exception {
        TextMessageResponse textMessageResponse1 = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageResponse1.setMessageStatus(MessageStatus.NEW);
        TextMessageResponse textMessageResponse2 = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageResponse2.setMessageStatus(MessageStatus.FETCHED);
        when(textMessageService.getReceivedMessages(RECEIVER)).thenReturn(List.of(textMessageResponse1, textMessageResponse2));

        mockMvc.perform(get("/messages/"+RECEIVER+"/new")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldGetSentMessages() throws Exception {
        TextMessageResponse textMessageResponse1 = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageResponse1.setMessageStatus(MessageStatus.NEW);
        TextMessageResponse textMessageResponse2 = new TextMessageResponse(MESSAGE_ID, SENDER, RECEIVER, MESSAGE_BODY, new Date());
        textMessageResponse2.setMessageStatus(MessageStatus.FETCHED);
        when(textMessageService.getSentMessages(SENDER)).thenReturn(List.of(textMessageResponse1, textMessageResponse2));

        mockMvc.perform(get("/messages/"+SENDER+"/sent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldDeleteMessages() throws Exception {
        doNothing().when(textMessageService).deleteMessages(isA(MessageIds.class));

        List<String> ids = List.of(MESSAGE_ID);
        MessageIds messageIds = new MessageIds();
        messageIds.setIds(ids);
        String requestBody = new ObjectMapper().writeValueAsString(messageIds);

        mockMvc.perform(delete("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMessageById() throws Exception {
        doNothing().when(textMessageService).deleteMessageById(isA(String.class));

        mockMvc.perform(delete("/messages/"+MESSAGE_ID))
                .andExpect(status().isOk());
    }

    private static String requestBody() throws JsonProcessingException {
        TextMessage textMessage = new TextMessage(SENDER, RECEIVER, MESSAGE_BODY);
        return new ObjectMapper().writeValueAsString(textMessage);
    }
}