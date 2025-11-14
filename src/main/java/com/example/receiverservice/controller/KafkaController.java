package com.example.receiverservice.controller;

import com.example.receiverservice.cache.MessageCacheService;
import com.example.receiverservice.entity.KafkaMessage;
import com.example.receiverservice.repository.KafkaMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class KafkaController {

    private final KafkaMessageRepository messageRepo;
    private final MessageCacheService cacheService;

    private final List<String> kafkaBuffer = new ArrayList<>(); // In-memory Kafka messages

    @Autowired
    public KafkaController(KafkaMessageRepository messageRepo, MessageCacheService cacheService) {
        this.messageRepo = messageRepo;
        this.cacheService = cacheService;
    }

    public void addKafkaMessage(String msg) {
        kafkaBuffer.add(msg);
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("kafkaMessages", kafkaBuffer);
        model.addAttribute("dbMessages", messageRepo.findAll());
        return "messages";
    }

    @PostMapping("/saveToDb")
    public String saveToDatabase(@RequestParam("message") String message) {
        KafkaMessage saved = messageRepo.save(new KafkaMessage(message, LocalDateTime.now()));
        return "redirect:/dashboard";
    }

    @PostMapping("/saveToRedis")
    public String saveToRedis() {
        List<KafkaMessage> messages = messageRepo.findAll();
        for (KafkaMessage msg : messages) {
            cacheService.cacheMessage(String.valueOf(msg.getId()), msg.getContent());
        }
        return "redirect:/dashboard";
    }
}
