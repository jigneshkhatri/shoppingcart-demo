package com.shoppingcart.inventory.entities;

import com.shoppingcart.inventory.entities.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

/**
 * @author Jigs
 */
@Entity
@Table(name = "products")
@Data
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
