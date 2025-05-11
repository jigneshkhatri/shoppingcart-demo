package com.shoppingcart.order.entities;

import com.shoppingcart.order.dtos.request.OrderItemDTO;
import com.shoppingcart.order.entities.common.AbstractEntity;
import com.shoppingcart.order.enums.StatusCode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Jigs
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "status")
    private StatusCode status;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "transaction_id")
    private String transactionId;

    @Transient
    private List<OrderItemDTO> orderItems;

    public Order(Order order) {
        this.deliveryAddress = order.getDeliveryAddress();
        this.status = order.getStatus();
    }
}
