package com.shoppingcart.inventory.exceptions;

/**
 * @author Jigs
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
