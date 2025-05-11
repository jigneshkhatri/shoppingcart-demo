package com.shoppingcart.order.exceptions;

import java.util.List;

/**
 * @author Jigs
 */
public class InvalidRequestBodyException extends RuntimeException {

    private final String errorCode;
    private final List<InvalidField> invalidFields;

    public InvalidRequestBodyException(String message, String errorCode, List<InvalidField> invalidFields) {
        super(message);
        this.errorCode = errorCode;
        this.invalidFields = invalidFields;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public List<InvalidField> getInvalidFields() {
        return invalidFields;
    }

    // Nested class to hold individual field errors
    public static class InvalidField {
        private final String fieldName;
        private final String errorMessage;

        public InvalidField(String fieldName, String errorMessage) {
            this.fieldName = fieldName;
            this.errorMessage = errorMessage;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
