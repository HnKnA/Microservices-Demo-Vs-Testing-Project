package com.example.inventory.service;

import com.example.common.event.OrderEvent;
import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void updateStock(OrderEvent event) {
        log.info("Received OrderEvent from Kafka: {}", event);

        // Find the inventory record or create it if not found
        Inventory inventory = inventoryRepository.findByProductName(event.getProductName())
            .orElseGet(() -> {
                Inventory newInv = new Inventory();
                newInv.setProductName(event.getProductName());
                newInv.setStock(10000); // Initial stock
                return newInv;
            });

        // Decrement stock
        inventory.setStock(inventory.getStock() - event.getQuantity());
        inventoryRepository.save(inventory);

        // For demonstration, log the new stock level
        log.info("Updated stock for product {}: {}", event.getProductName(), inventory.getStock());
    }

    public boolean checkStock(String productName, int quantity) {
        Optional<Inventory> inventory = inventoryRepository.findByProductName(productName);
        return inventory.map(inv -> inv.getStock() >= quantity).orElse(false);
    }
}
