package com.shoppingcart.order.dtos.response;

import com.shoppingcart.order.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jigs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {

    private StatusCode code;
    private String message;
}
