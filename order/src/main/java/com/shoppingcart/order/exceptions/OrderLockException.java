package com.shoppingcart.order.exceptions;

/**
 * @author Jigs
 */
public class OrderLockException extends RuntimeException {

    public OrderLockException(String message) {
        super(message);
    }
}
