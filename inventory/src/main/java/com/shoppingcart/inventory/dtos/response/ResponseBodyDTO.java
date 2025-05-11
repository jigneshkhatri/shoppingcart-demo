package com.shoppingcart.inventory.dtos.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jigs
 */
@Getter
@Setter
public class ResponseBodyDTO<E extends Object> {

    /** The data. */
    private E data;

    /** The message. */
    private String message;

    /** The error. */
    private ApiErrorDTO error;

    /**
     * Instantiates a new response body.
     */
    public ResponseBodyDTO() {
    }

    /**
     * Instantiates a new response body.
     *
     * @param data the data
     */
    public ResponseBodyDTO(E data) {
        this.data = data;
    }

    /**
     * Instantiates a new response body.
     *
     * @param error the error
     */
    public ResponseBodyDTO(ApiErrorDTO error) {
        this.error = error;
    }

    /**
     * Instantiates a new response body.
     *
     * @param data    the data
     * @param message the message
     */
    public ResponseBodyDTO(E data, String message) {
        this.data = data;
        this.message = message;
    }

}
