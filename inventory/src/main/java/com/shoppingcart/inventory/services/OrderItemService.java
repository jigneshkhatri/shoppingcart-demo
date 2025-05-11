package com.shoppingcart.inventory.services;

import com.shoppingcart.inventory.dtos.OrderCreatedEvent;
import com.shoppingcart.inventory.entities.OrderItem;

import java.util.List;

/**
 * @author Jigs
 */
public interface OrderItemService {
    void save(Long orderId, List<OrderItem> orderItems);

    void processOrderItems(OrderCreatedEvent orderCreatedEvent);
}
