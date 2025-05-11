package com.shoppingcart.order.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shoppingcart.order.clients.InventoryClient;
import com.shoppingcart.order.clients.KafkaEventPublisherClient;
import com.shoppingcart.order.configs.RequestContext;
import com.shoppingcart.order.constants.Entity;
import com.shoppingcart.order.dtos.request.OrderItemDTO;
import com.shoppingcart.order.dtos.response.PlaceOrderResponseDTO;
import com.shoppingcart.order.dtos.response.StatusDTO;
import com.shoppingcart.order.entities.Order;
import com.shoppingcart.order.enums.StatusCode;
import com.shoppingcart.order.exceptions.OrderLockException;
import com.shoppingcart.order.messages.AbstractEntityStatusMessage;
import com.shoppingcart.order.messages.EntityStatusMessageFactory;
import com.shoppingcart.order.repositories.OrderRepository;
import com.shoppingcart.order.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jigs
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private InventoryClient inventoryClient;
    private EntityStatusMessageFactory entityStatusMessageFactory;
    private AbstractEntityStatusMessage orderStatusMessage;
    private KafkaEventPublisherClient kafkaEventPublisherClient;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryClient inventoryClient,
                            EntityStatusMessageFactory entityStatusMessageFactory, KafkaEventPublisherClient kafkaEventPublisherClient) {
        // Constructor
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.orderStatusMessage = entityStatusMessageFactory.getEntityStatusMessage(Entity.ORDER);
        this.kafkaEventPublisherClient = kafkaEventPublisherClient;
    }

    @Override
    @Transactional
    public PlaceOrderResponseDTO save(Order order) {
        /**
         * Order life cycle:
         * PENDING -> LOCKED -> CONFIRMED/FAILED
         *
         * Step 1: Save the order details
         * Step 2: Call the inventory API to save the unprocessed order items
         * Step 3: Push the order saved event to kafka queue
         * Step 4: Build and return the order placement response
         */

        // Step 1: Save the order details
        order.setStatus(StatusCode.PENDING);
        order.setId(null);
        order.setCreatedBy(RequestContext.getUserId());
        order.setUpdatedBy(RequestContext.getUserId());
        Order savedOrder = orderRepository.save(order);

        // Step 4: Build and return the order placement response
        return PlaceOrderResponseDTO.builder()
                .id(savedOrder.getId())
                .status(new StatusDTO(savedOrder.getStatus(), this.orderStatusMessage.getMessage(savedOrder.getStatus())))
                .build();
    }

    @Override
    @Transactional
    public void lock(Long orderId, String transactionId) {
        int updatedCount = this.orderRepository.lock(orderId, StatusCode.PENDING, transactionId, LocalDateTime.now());
        if (updatedCount == 0) {
            throw new OrderLockException("Order is not in pending state or already locked");
        }
    }

    @Override
    @Transactional
    public void unlockOrderAndUpdateStatus(Long orderId, StatusCode status, String transactionId) {
        this.orderRepository.unlockAndUpdateStatus(orderId, status, transactionId, LocalDateTime.now());
    }

    @Override
    public void saveOrderItems(Long orderId, List<OrderItemDTO> orderItems) throws JsonProcessingException {
        // Call the inventory API to save the unprocessed order items
        this.inventoryClient.saveOrderItems(orderId, orderItems);

        // Push the order saved event to kafka queue
        this.kafkaEventPublisherClient.publishNewOrderEvent(orderId);
    }
}
