package com.shoppingcart.order.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shoppingcart.order.dtos.request.OrderItemDTO;
import com.shoppingcart.order.dtos.response.PlaceOrderResponseDTO;
import com.shoppingcart.order.entities.Order;
import com.shoppingcart.order.enums.StatusCode;

import java.util.List;

/**
 * @author Jigs
 */
public interface OrderService {
    PlaceOrderResponseDTO save(Order order);
    void lock(Long orderId, String transactionId);
    void unlockOrderAndUpdateStatus(Long orderId, StatusCode status, String transactionId);
    void saveOrderItems(Long orderId, List<OrderItemDTO> orderItems) throws JsonProcessingException;
}
