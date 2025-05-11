package com.shoppingcart.inventory.entities;

import com.shoppingcart.inventory.entities.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Jigs
 */
@Entity
@Table(name = "inventory")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Version
    private Integer version;
}
