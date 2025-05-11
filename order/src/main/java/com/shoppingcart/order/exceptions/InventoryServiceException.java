package com.shoppingcart.order.exceptions;

import org.springframework.web.client.HttpStatusCodeException;

/**
 * @author Jigs
 */
public class InventoryServiceException extends RuntimeException {
    public InventoryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
