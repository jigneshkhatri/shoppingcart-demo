package com.shoppingcart.order.messages;

import com.shoppingcart.order.enums.StatusCode;
import com.shoppingcart.order.exceptions.InvalidStatusException;

/**
 * @author Jigs
 */
public class OrderStatusMessage implements AbstractEntityStatusMessage {
    @Override
    public String getMessage(StatusCode status) {
        switch (status) {
            case PENDING:
                return "Order saved";
            case CONFIRMED:
                return "Order confirmed";
            default:
                throw new InvalidStatusException("Invalid status: " + status);
        }
    }
}
