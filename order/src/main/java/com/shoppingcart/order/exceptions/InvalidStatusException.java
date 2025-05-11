package com.shoppingcart.order.exceptions;

/**
 * @author Jigs
 */
public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException(String message) {
        super(message);
    }
}
