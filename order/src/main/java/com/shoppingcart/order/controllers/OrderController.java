package com.shoppingcart.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shoppingcart.order.constants.ApiVersion;
import com.shoppingcart.order.dtos.response.PlaceOrderResponseDTO;
import com.shoppingcart.order.dtos.response.ResponseBodyDTO;
import com.shoppingcart.order.entities.Order;
import com.shoppingcart.order.enums.StatusCode;
import com.shoppingcart.order.exceptions.InvalidRequestBodyException;
import com.shoppingcart.order.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jigs
 */
@RestController
@RequestMapping("orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(ApiVersion.V1)
    public ResponseEntity<ResponseBodyDTO<PlaceOrderResponseDTO>> save(@RequestBody Order order) throws JsonProcessingException {
        // validate request body
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new InvalidRequestBodyException("Order items are required", "ERROR",
                    List.of(new InvalidRequestBodyException.InvalidField("orderItems", "Order items cannot be null or empty")));
        }

        // save order
        PlaceOrderResponseDTO placeOrderResponseDTO = this.orderService.save(order);

        // save order items
        this.orderService.saveOrderItems(placeOrderResponseDTO.getId(), order.getOrderItems());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseBodyDTO<>(placeOrderResponseDTO));
    }

    @GetMapping(ApiVersion.V1 + "/{orderId}/lock")
    public ResponseEntity<Void> lock(@PathVariable("orderId") Long orderId,
                                     @RequestParam(value = "transactionId", required = true) String transactionId) {
        this.orderService.lock(orderId, transactionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(ApiVersion.V1 + "/{orderId}/unlock")
    public ResponseEntity<Void> unlockOrderAndUpdateStatus(@PathVariable("orderId") Long orderId,
                                       @RequestParam(value = "transactionId", required = true) String transactionId,
                                       @RequestParam(value = "status", required = true) StatusCode status) {
        this.orderService.unlockOrderAndUpdateStatus(orderId, status, transactionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
