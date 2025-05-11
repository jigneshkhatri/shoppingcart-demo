package com.shoppingcart.inventory.exceptions;

/**
 * @author Jigs
 */
public class OrderServiceException extends RuntimeException {
    public OrderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
