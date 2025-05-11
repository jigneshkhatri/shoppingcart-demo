package com.shoppingcart.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shoppingcart.order.clients.InventoryClient;
import com.shoppingcart.order.clients.KafkaEventPublisherClient;
import com.shoppingcart.order.dtos.request.OrderItemDTO;
import com.shoppingcart.order.dtos.response.PlaceOrderResponseDTO;
import com.shoppingcart.order.dtos.response.ResponseBodyDTO;
import com.shoppingcart.order.dtos.response.StatusDTO;
import com.shoppingcart.order.entities.Order;
import com.shoppingcart.order.enums.StatusCode;
import com.shoppingcart.order.exceptions.InvalidRequestBodyException;
import com.shoppingcart.order.services.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author Jigs
 */
@ExtendWith({MockitoExtension.class})
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;


    /**
     * =============================================
     * Add new order tests
     * =============================================
     * */

    // add new order happy scenario
    @Test
    @DisplayName("should successfully add new order")
    void addNewOrder_shouldSuccessfullyAddNewOrder() throws JsonProcessingException {
        // given
        Order order = new Order();
        List<OrderItemDTO> orderItems = List.of(new OrderItemDTO(1L, 5), new OrderItemDTO(2L, 3));
        order.setOrderItems(orderItems);
        StatusDTO status = new StatusDTO();
        status.setCode(StatusCode.PENDING);
        status.setMessage("Order placed successfully");
        PlaceOrderResponseDTO placeOrderResponseObj = PlaceOrderResponseDTO
                .builder()
                .id(1L)
                .status(status)
                .build();
        placeOrderResponseObj.setStatus(status);

        // when
        when(this.orderService.save(order)).thenReturn(placeOrderResponseObj);
        doNothing().when(orderService).saveOrderItems(placeOrderResponseObj.getId(), orderItems);

        // action
        ResponseEntity<ResponseBodyDTO<PlaceOrderResponseDTO>> placeOrderResponseResponseEntity = this.orderController.save(order);

        // then
        assertThat(placeOrderResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(placeOrderResponseResponseEntity.getBody()).isNotNull();
        PlaceOrderResponseDTO placeOrderResponse = placeOrderResponseResponseEntity.getBody().getData();
        assertThat(placeOrderResponse.getId()).isNotNull();
        assertThat(placeOrderResponse.getStatus()).isNotNull();
        assertThat(placeOrderResponse.getStatus().getCode()).isEqualTo(StatusCode.PENDING);
    }

    // add new order with invalid request body
    @Test
    @DisplayName("should return bad request when invalid request body is provided")
    void addNewOrder_shouldReturnBadRequest() {
        // given
        Order order = new Order();

        // action and then
        assertThrows(InvalidRequestBodyException.class, () -> {
            this.orderController.save(order);
        });
    }

    /**
     * =============================================
     * View all orders tests
     * =============================================
     * */

    // view all the orders for the user
    void viewAllOrders_shouldReturnAllOrders() {
        // given
        // when
        // action
        // then
    }

    // view all the orders for the user, but there are no orders available
    void viewAllOrders_shouldReturnNoOrders() {
        // given
        // when
        // action
        // then
    }

    /**
     * =============================================
     * View single order tests
     * =============================================
     * */

    // view order by order id
    void viewOrderById_shouldReturnOrder() {
        // given
        // when
        // action
        // then
    }

    // view order but order id is not found
    void viewOrderById_shouldReturnOrderNotFound() {
        // given
        // when
        // action
        // then
    }

    // view order but order id is invalid
    void viewOrderById_shouldReturnBadRequest() {
        // given
        // when
        // action
        // then
    }


    /**
     * =============================================
     * Update existing order tests
     * =============================================
     * */

    /**
     * =============================================
     * Delete existing order tests
     * =============================================
     * */

    /**
     * =============================================
     * Update existing order status tests
     * =============================================
     * */

}
