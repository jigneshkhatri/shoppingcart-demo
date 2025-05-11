package com.shoppingcart.order.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.order.dtos.OrderCreatedEvent;
import com.shoppingcart.order.entities.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Jigs
 */
@Slf4j
@Component
public class KafkaEventPublisherClient {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.new-orders}")
    private String newOrderTopic;

    public KafkaEventPublisherClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void publishNewOrderEvent(Long orderId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String eventJson = mapper.writeValueAsString(new OrderCreatedEvent(orderId));
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(newOrderTopic, eventJson);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send event to Kafka topic [{}]: {}", newOrderTopic, ex.getMessage(), ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                ProducerRecord<String, String> record = result.getProducerRecord();
                log.info("Event sent to Kafka topic [{}] at partition [{}], offset [{}]",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

}
