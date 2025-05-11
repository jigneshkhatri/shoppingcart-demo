package com.shoppingcart.order.controllers.common;

import com.shoppingcart.order.dtos.response.ApiErrorDTO;
import com.shoppingcart.order.dtos.response.ResponseBodyDTO;
import com.shoppingcart.order.exceptions.InvalidEntityException;
import com.shoppingcart.order.exceptions.InvalidRequestBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Jigs
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ResponseBodyDTO<ApiErrorDTO>> handleInvalidEntityException(InvalidEntityException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBodyDTO<>(
                new ApiErrorDTO.ApiErrorBuilder().errorCode("INTERNAL_ERROR").debugMessage(ex.getMessage()).build()));
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ResponseBodyDTO<ApiErrorDTO>> handleInvalidRequestBodyException(InvalidRequestBodyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDTO<>(
                new ApiErrorDTO.ApiErrorBuilder().invalidFields(ex.getInvalidFields()).errorCode(ex.getErrorCode()).debugMessage(ex.getMessage()).build()));
    }
}
