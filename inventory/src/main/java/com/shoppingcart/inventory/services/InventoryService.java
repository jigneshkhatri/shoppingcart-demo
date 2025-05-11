package com.shoppingcart.inventory.services;

import com.shoppingcart.inventory.entities.OrderItem;

import java.util.List;

/**
 * @author Jigs
 */
public interface InventoryService {
    void updateInventory(OrderItem orderItem);
}
