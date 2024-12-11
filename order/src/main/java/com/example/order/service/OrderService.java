package com.example.order.service;

import com.example.common.event.OrderEvent;
import com.example.order.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createOrder(OrderRequest request) {
        // Validate and save the order (omitted for brevity)
        OrderEvent event = new OrderEvent(request.getOrderId(), request.getProductId(), request.getQuantity());

        // Log the event we're sending
        log.info("Sending OrderEvent to Kafka: {}", event);

        // Send the message to the Kafka topic
        kafkaTemplate.send("order-topic", event);
    }
}
