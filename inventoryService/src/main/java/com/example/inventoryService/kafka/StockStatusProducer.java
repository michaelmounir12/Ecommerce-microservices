package com.example.inventoryService.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockStatusProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendStockStatus(StockStatusEvent event) {
        try {
            String message = mapper.writeValueAsString(event);
            kafkaTemplate.send("stock-status-topic", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}