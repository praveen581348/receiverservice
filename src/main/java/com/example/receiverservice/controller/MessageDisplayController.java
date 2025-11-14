package com.example.receiverservice.controller;

import com.example.receiverservice.entity.KafkaMessage;
import com.example.receiverservice.repository.KafkaMessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MessageDisplayController {

    private final KafkaMessageRepository kafkaMessageRepository;

    public MessageDisplayController(KafkaMessageRepository kafkaMessageRepository) {
        this.kafkaMessageRepository = kafkaMessageRepository;
    }

    @GetMapping("/messages")
    public String showMessages(Model model) {
        List<KafkaMessage> messages = kafkaMessageRepository.findAll();
        model.addAttribute("messages", messages);
        return "messages"; // This will render the messages.html Thymeleaf template
    }
}