package com.shoppingcart.inventory.services.impl;

import com.shoppingcart.inventory.clients.OrderClient;
import com.shoppingcart.inventory.configs.RequestContext;
import com.shoppingcart.inventory.dtos.OrderCreatedEvent;
import com.shoppingcart.inventory.entities.OrderItem;
import com.shoppingcart.inventory.enums.StatusCode;
import com.shoppingcart.inventory.repositories.OrderItemRepository;
import com.shoppingcart.inventory.services.InventoryService;
import com.shoppingcart.inventory.services.OrderItemService;
import com.shoppingcart.inventory.utils.RandomStringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jigs
 */
@Service
@Transactional(readOnly = true)
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemRepository orderItemRepository;
    private OrderClient orderClient;
    private InventoryService inventoryService;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderClient orderClient, InventoryService inventoryService) {
        this.orderItemRepository = orderItemRepository;
        this.orderClient = orderClient;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public void save(Long orderId, List<OrderItem> orderItems) {
        /**
         * Order item life cycle:
         * PENDING -> PARTIAL/CONFIRMED/FAILED
         *
         * Step 1: Save the order items
         */

        // Step 1: Save the order items
        for (OrderItem orderItem : orderItems) {
            orderItem.setId(null);
            orderItem.setOrderId(orderId);
            orderItem.setStatus(StatusCode.PENDING);
            orderItem.setCreatedBy(RequestContext.getUserId());
            orderItem.setUpdatedBy(RequestContext.getUserId());
            orderItem.setErrorMessage(null);
        }
        this.orderItemRepository.saveAll(orderItems);
    }

    @Override
    @Transactional
    public void processOrderItems(OrderCreatedEvent orderCreatedEvent) {
        /**
         * Step 1: Lock the order
         * Step 2: Lock and fetch all the order items for the order
         * Step 3: Process each order item
         * Step 4: Unlock the order
         * Step 5: Save the order items
         */
        Long orderId = orderCreatedEvent.getId();
        String transactionId = RandomStringGenerator.generate(6) + "" + orderId;

        // Step 1: Lock the order
        try {
            this.orderClient.lockOrder(orderId, transactionId);
        } catch(Exception e) {
            // order might be already locked
            e.printStackTrace();
            throw e;
        }

        // Step 2: Lock and fetch all the order items for the order
        // Lock order items
        int updatedCount = this.orderItemRepository.lock(orderId, StatusCode.PENDING, transactionId, LocalDateTime.now());

        if (updatedCount == 0) {
            // Either no order items to process or order items are already locked
            // unlock the order
            this.orderClient.unlockOrderAndUpdateStatus(orderId, StatusCode.PENDING, transactionId);
            return;
        }

        List<OrderItem> orderItems = this.orderItemRepository.findByOrderId(orderId);
        for (OrderItem orderItem : orderItems) {
            // Step 3: Process each order item
            try {
                // Call the inventory service to update the inventory
                this.inventoryService.updateInventory(orderItem);
            } catch (Exception e) {
                // Silently Handle exception to prevent transaction rollback
                e.printStackTrace();
            }
        }

        StatusCode orderStatus = StatusCode.CONFIRMED;
        if (orderItems.stream().anyMatch(x -> x.getStatus().equals(StatusCode.PARTIAL))) {
            orderStatus = StatusCode.PARTIAL;
        } else if (orderItems.stream().allMatch(x -> x.getStatus().equals(StatusCode.FAILED))) {
            orderStatus = StatusCode.FAILED;
        }

        // Step 4: Unlock the order
        this.orderClient.unlockOrderAndUpdateStatus(orderId, orderStatus, transactionId);

        // Step 5: Save the order items
        orderItems.forEach(x -> x.setIsLocked(false));
        this.orderItemRepository.saveAll(orderItems);
    }
}
