package com.example.receiverservice.repository;

import com.example.receiverservice.entity.KafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaMessageRepository extends JpaRepository<KafkaMessage, Long> {
}