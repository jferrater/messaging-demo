package com.github.jferrater.messagingservice.repository;

import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextMessageRepository extends MongoRepository<TextMessageEntity, String> {

    List<TextMessageEntity> findMessagesByReceiver(String receiver);

    List<TextMessageEntity> findMessagesBySender(String sender);
}
