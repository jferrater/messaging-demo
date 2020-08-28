package com.github.jferrater.messagingservice.service;

import com.github.jferrater.messagingservice.repository.TextMessageRepository;
import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.springframework.stereotype.Service;

@Service
public class TextMessageService {

    private TextMessageRepository textMessageRepository;

    public TextMessageService(TextMessageRepository textMessageRepository) {
        this.textMessageRepository = textMessageRepository;
    }

    public TextMessageEntity createMessage(TextMessageEntity textMessageEntity) {
        return textMessageRepository.insert(textMessageEntity);
    }
}
