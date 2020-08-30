package com.github.jferrater.messagingservice.controller;

import com.github.jferrater.messagingservice.model.*;
import com.github.jferrater.messagingservice.service.TextMessageService;
import com.mongodb.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@Tag(name = "messaging_service", description = "The Text Messaging Service API")
public class TextMessageController {

    private TextMessageService textMessageService;

    public TextMessageController(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @Operation(
            summary = "Send a text message",
            description = "Returns the text message information",
            tags = "messaging_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TextMessageResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @PostMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextMessageResponse> sendTextMessage(@Valid @RequestBody TextMessage textMessage) {
        TextMessageResponse textMessageResponse = textMessageService.createMessage(textMessage);
        return new ResponseEntity<>(textMessageResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get the received text messages",
            description = "Returns the list of text messages received by the receiver regardless of the message status. Filter messages by start and stop dates.",
            tags = "messaging_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = TextMessageResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping(value = "/messages/{username}/received", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getReceivedMessages(
            @Parameter(description = "The username of the receiver") @PathVariable("username") String username,
            @Parameter(description = "The start date (optional)") @Nullable @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) Date startDate,
            @Parameter(description = "The stop date (optional)") @Nullable @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) Date stopDate) {
        if(startDate != null && stopDate != null) {
            List<TextMessageResponse> receivedMessagesBetweenDates = textMessageService.getReceivedMessagesBetweenDates(username, startDate, stopDate);
            return new ResponseEntity<>(receivedMessagesBetweenDates, HttpStatus.OK);
        } else {
            List<TextMessageResponse> messages = textMessageService.getReceivedMessages(username);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Get the newly received text messages",
            description = "Returns the list of newly received text messages.",
            tags = "messaging_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = TextMessageResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping(value = "/messages/{username}/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getNewMessages(
            @Parameter(description = "The username of the receiver") @PathVariable("username") String username) {
        List<TextMessageResponse> messages = textMessageService.getReceivedMessages(username);
        List<TextMessageResponse> newMessages = messages.stream()
                .filter(message -> MessageStatus.NEW.equals(message.getMessageStatus()))
                .collect(toList());
        List<TextMessageResponse> toBeUpdatedList = new ArrayList<>(newMessages);
        toBeUpdatedList.forEach(m -> {
            m.setMessageStatus(MessageStatus.FETCHED);
            textMessageService.updateMessage(m);
        });
        return new ResponseEntity<>(newMessages, HttpStatus.OK);
    }

    @Operation(
            summary = "Get the sent text messages by the sender",
            description = "Returns the list of sent text messages.",
            tags = "messaging_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = TextMessageResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @GetMapping(value = "/messages/{username}/sent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TextMessageResponse>> getSentMessages(@Parameter(description = "The username of the sender") @PathVariable("username") String username) {
        List<TextMessageResponse> sentMessages = textMessageService.getSentMessages(username);
        return new ResponseEntity<>(sentMessages, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete text messages",
            description = "Delete the text messages",
            tags = "messaging_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping(value = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@RequestBody MessageIds messageIds) {
        textMessageService.deleteMessages(messageIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a text message by id",
            description = "Delete a text message by id",
            tags = "messaging_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiError.class)))
            }
    )
    @DeleteMapping(value = "/messages/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "The id of the text message") @PathVariable("id") String id) {
        textMessageService.deleteMessageById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
