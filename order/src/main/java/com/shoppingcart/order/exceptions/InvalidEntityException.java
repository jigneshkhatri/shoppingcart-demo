package com.shoppingcart.order.exceptions;

/**
 * @author Jigs
 */
public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String message) {
        super(message);
    }
}
