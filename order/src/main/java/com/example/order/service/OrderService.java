package com.example.order.service;

import com.example.common.event.OrderEvent;
import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final OrderRepository orderRepository;

    public OrderService(KafkaTemplate<String, OrderEvent> kafkaTemplate, OrderRepository orderRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderRepository = orderRepository;
    }

    public void createOrder(OrderRequest request) {

        // Check if the order already exists
        if (orderRepository.existsById(request.getOrderId())) {
            throw new IllegalStateException("Order with ID " + request.getOrderId() + " already exists!");
        }

        // Save the new order in the database
        Order order = new Order(request.getOrderId(), request.getProductName(), request.getQuantity());
        orderRepository.save(order);
        log.info("Order saved to DB: {}", order);

        // Create and log the event we are sending
        OrderEvent event = new OrderEvent(
            request.getOrderId(), request.getProductName(), request.getQuantity()
        );
        log.info("Sending OrderEvent to Kafka: {}", event);
        // Send the message to the Kafka topic
        kafkaTemplate.send("order-topic", event);
    }
}
