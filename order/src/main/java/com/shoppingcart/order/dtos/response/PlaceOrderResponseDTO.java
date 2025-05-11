package com.shoppingcart.order.dtos.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author Jigs
 */
@Data
@Builder
public class PlaceOrderResponseDTO {
    private Long id;
    private StatusDTO status;
}
