package com.github.jferrater.messagingservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.github.jferrater.messagingservice.repository")
public class AppConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoDbConnectionUri;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoDbConnectionUri);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), databaseName);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
