package com.example.receiverservice.kafka;

import com.example.receiverservice.controller.KafkaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiverService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverService.class);

    private final KafkaController kafkaController;

    public ReceiverService(KafkaController kafkaController) {
        this.kafkaController = kafkaController;
    }

    @KafkaListener(topics = "test-topic", groupId = "receiver-group")
    public void receiveMessage(String message) {
        logger.info("Received message from Kafka: {}", message);

        // ✅ Store Kafka message in memory via controller
        kafkaController.addKafkaMessage(message);
    }
}
