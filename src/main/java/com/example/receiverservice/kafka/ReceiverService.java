package com.example.receiverservice.kafka;

import com.example.receiverservice.entity.KafkaMessage;
import com.example.receiverservice.repository.KafkaMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReceiverService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverService.class);

    private final KafkaMessageRepository kafkaMessageRepository;

    public ReceiverService(KafkaMessageRepository kafkaMessageRepository) {
        this.kafkaMessageRepository = kafkaMessageRepository;
    }

    @KafkaListener(topics = "test-topic", groupId = "receiver-group")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
        KafkaMessage kafkaMessage = new KafkaMessage(message, LocalDateTime.now());
        kafkaMessageRepository.save(kafkaMessage);
        logger.info("Message saved to database: {}", message);
    }
}