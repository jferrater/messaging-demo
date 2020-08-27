package com.github.jferrater.messagingservice.repository;

import com.github.jferrater.messagingservice.repository.document.TextMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextMessageRepository extends MongoRepository<TextMessageEntity, String> {
}
