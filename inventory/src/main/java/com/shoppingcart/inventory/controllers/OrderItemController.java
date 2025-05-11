package com.shoppingcart.inventory.controllers;

import com.shoppingcart.inventory.constants.ApiVersion;
import com.shoppingcart.inventory.dtos.response.ResponseBodyDTO;
import com.shoppingcart.inventory.entities.OrderItem;
import com.shoppingcart.inventory.services.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jigs
 */
@RestController
@RequestMapping("order-items")
public class OrderItemController {

    private OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }
    @PostMapping(ApiVersion.V1 + "/{orderId}")
    public ResponseEntity<Void> save(@PathVariable("orderId") Long orderId,
                                     @RequestBody List<OrderItem> orderItems) {
        this.orderItemService.save(orderId, orderItems);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
