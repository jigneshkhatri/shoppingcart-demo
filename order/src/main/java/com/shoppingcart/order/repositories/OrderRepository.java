package com.shoppingcart.order.repositories;

import com.shoppingcart.order.entities.Order;
import com.shoppingcart.order.enums.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author Jigs
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Query("UPDATE Order o " +
            "SET o.isLocked=true, o.transactionId=:transactionId, o.updatedOn=:now " +
            "WHERE o.id = :orderId and o.status = :pendingStatus and (o.isLocked IS NULL OR o.isLocked = false)")
    int lock(Long orderId, StatusCode pendingStatus, String transactionId, LocalDateTime now);

    @Modifying
    @Query("UPDATE Order o " +
            "SET o.isLocked=false, o.transactionId=null, o.updatedOn=:now, o.status=:status " +
            "WHERE o.id = :orderId and o.isLocked = true and o.transactionId = :transactionId")
    int unlockAndUpdateStatus(Long orderId, StatusCode status, String transactionId, LocalDateTime now);
}
