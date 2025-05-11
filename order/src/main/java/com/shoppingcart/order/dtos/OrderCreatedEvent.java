package com.shoppingcart.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jigs
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long id;
}
