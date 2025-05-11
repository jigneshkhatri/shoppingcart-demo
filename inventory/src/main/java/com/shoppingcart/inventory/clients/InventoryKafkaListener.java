package com.shoppingcart.inventory.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.inventory.dtos.OrderCreatedEvent;
import com.shoppingcart.inventory.services.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author Jigs
 */
@Slf4j
@Component
public class InventoryKafkaListener {

    @Value("${kafka.consumer-group}")
    private String consumerGroup;

    private OrderItemService orderItemService;

    public InventoryKafkaListener(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @KafkaListener(
            topics = "${kafka.topic.new-orders}",
            groupId = "${kafka.consumer-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            log.info("Consuming message: {}", record.value());

            ObjectMapper objectMapper = new ObjectMapper();
            OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(record.value().toString(), OrderCreatedEvent.class);
            // Your message processing logic
            this.orderItemService.processOrderItems(orderCreatedEvent);

            // Only commit if processing was successful
            acknowledgment.acknowledge();

        } catch (Exception ex) {
            log.error("Failed to process Kafka message: {}", record.value(), ex);
            ex.printStackTrace();
            // Message will not be acknowledged; will be retried based on Kafka settings
        }
    }
}
