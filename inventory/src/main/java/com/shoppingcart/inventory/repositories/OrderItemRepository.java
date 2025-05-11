package com.shoppingcart.inventory.repositories;

import com.shoppingcart.inventory.entities.OrderItem;
import com.shoppingcart.inventory.enums.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jigs
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Modifying
    @Query("UPDATE OrderItem o " +
            "SET o.isLocked = true, o.transactionId=:transactionId, o.updatedOn=:now " +
            "WHERE o.orderId = :orderId and o.status = :pendingStatus and (o.isLocked IS NULL OR o.isLocked = false)")
    int lock(Long orderId, StatusCode pendingStatus, String transactionId, LocalDateTime now);

    List<OrderItem> findByOrderId(Long orderId);
}
