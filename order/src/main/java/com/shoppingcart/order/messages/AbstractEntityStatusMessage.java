package com.shoppingcart.order.messages;

import com.shoppingcart.order.enums.StatusCode;

/**
 * @author Jigs
 */
public interface AbstractEntityStatusMessage {
    String getMessage(StatusCode status);
}
