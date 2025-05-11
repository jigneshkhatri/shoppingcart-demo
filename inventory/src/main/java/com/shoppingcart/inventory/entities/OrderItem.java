package com.shoppingcart.inventory.entities;

import com.shoppingcart.inventory.entities.common.AbstractEntity;
import com.shoppingcart.inventory.enums.StatusCode;
import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Jigs
 */
@Entity
@Table(name = "order_items")
@Data
public class OrderItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "status")
    private StatusCode status;

    @Column(name = "error_message")
    private String errorMessage;

}
