package com.example.inventory.service;

import com.example.common.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class InventoryService {
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void updateStock(OrderEvent event) {
        log.info("Received OrderEvent from Kafka: {}", event);

        // Decrement stock for the product
        stock.put(event.getProductId(), stock.getOrDefault(event.getProductId(), 10000) - event.getQuantity());

        // For demonstration, log the new stock level
        log.info("Updated stock for product {}: {}", event.getProductId(), stock.get(event.getProductId()));
    }

    public boolean checkStock(String productId, int quantity) {
        return stock.getOrDefault(productId, 0) >= quantity;
    }
}
