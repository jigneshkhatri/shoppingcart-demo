package com.shoppingcart.order.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jigs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long productId;
    private Integer quantity;
}
