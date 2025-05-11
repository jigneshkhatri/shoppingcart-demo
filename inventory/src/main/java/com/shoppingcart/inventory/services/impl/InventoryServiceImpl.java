package com.shoppingcart.inventory.services.impl;

import com.shoppingcart.inventory.entities.Inventory;
import com.shoppingcart.inventory.entities.OrderItem;
import com.shoppingcart.inventory.enums.StatusCode;
import com.shoppingcart.inventory.exceptions.ProductNotFoundException;
import com.shoppingcart.inventory.repositories.InventoryRepository;
import com.shoppingcart.inventory.services.InventoryService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ConcurrentModificationException;

/**
 * @author Jigs
 */
@Service
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    @Retryable(
            value = { ConcurrentModificationException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void updateInventory(OrderItem orderItem) {
        try {
            Inventory inventory = this.inventoryRepository.findByProductId(orderItem.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Invalid productId: " + orderItem.getProductId()));

            int quantityAfterUpdate = inventory.getQuantity() - orderItem.getQuantity();
            StatusCode orderItemStatus = StatusCode.CONFIRMED;
            String errorMessage = null;
            if (quantityAfterUpdate < 0) {
                quantityAfterUpdate = 0;
                orderItemStatus = StatusCode.PARTIAL;
                errorMessage = "Insufficient quantity. Requested: " + orderItem.getQuantity() + ", Available: " + inventory.getQuantity();
            }

            inventory.setQuantity(quantityAfterUpdate);
            inventoryRepository.save(inventory);

            orderItem.setStatus(orderItemStatus);
            orderItem.setErrorMessage(errorMessage);
        } catch (OptimisticLockingFailureException e) {
            // Handle concurrent modification
            throw new ConcurrentModificationException("Concurrent modification exception");
        } catch (ProductNotFoundException ex) {
            orderItem.setStatus(StatusCode.FAILED);
            orderItem.setErrorMessage("Product not found");
        }
    }

}
