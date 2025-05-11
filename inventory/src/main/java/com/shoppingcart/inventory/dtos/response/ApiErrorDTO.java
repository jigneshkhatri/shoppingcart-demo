package com.shoppingcart.inventory.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shoppingcart.inventory.exceptions.InvalidRequestBodyException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jigs
 */
public class ApiErrorDTO implements Serializable {

    /** The message. */
    private String message;

    /** The timestamp. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    /** The debug message. */
    private String debugMessage;

    /** The error code. */
    private String errorCode;

    private List<InvalidRequestBodyException.InvalidField> invalidFields;

    /**
     * Instantiates a new API error.
     *
     * @param apiErrorBuilder the api error builder
     */
    private ApiErrorDTO(ApiErrorBuilder apiErrorBuilder) {
        this.debugMessage = apiErrorBuilder.debugMessage;
        this.errorCode = apiErrorBuilder.errorCode;
        this.message = apiErrorBuilder.message;
        this.timestamp = apiErrorBuilder.timestamp;
        this.invalidFields = apiErrorBuilder.invalidFields;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the debug message.
     *
     * @return the debug message
     */
    public String getDebugMessage() {
        return debugMessage;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    public List<InvalidRequestBodyException.InvalidField> getInvalidFields() {
        return invalidFields;
    }

    /**
     * The Class APIErrorBuilder.
     */
    public static class ApiErrorBuilder {

        /** The message. */
        private String message;

        /** The debug message. */
        private String debugMessage;

        /** The error code. */
        private String errorCode;

        /** The timestamp. */
        private LocalDateTime timestamp;

        private List<InvalidRequestBodyException.InvalidField> invalidFields;

        /**
         * Instantiates a new API error.
         */
        public ApiErrorBuilder() {
            this.timestamp = LocalDateTime.now();
        }

        /**
         * Message.
         *
         * @param message the message
         * @return the API error builder
         */
        public ApiErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Debug message.
         *
         * @param debugMessage the debug message
         * @return the API error builder
         */
        public ApiErrorBuilder debugMessage(String debugMessage) {
            this.debugMessage = debugMessage;
            return this;
        }

        /**
         * Error code.
         *
         * @param errorCode the error code
         * @return the API error builder
         */
        public ApiErrorBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ApiErrorBuilder invalidFields(List<InvalidRequestBodyException.InvalidField> invalidFields) {
            this.invalidFields = invalidFields;
            return this;
        }

        /**
         * Builds the.
         *
         * @return the API error
         */
        public ApiErrorDTO build() {
            return new ApiErrorDTO(this);
        }

    }
}
