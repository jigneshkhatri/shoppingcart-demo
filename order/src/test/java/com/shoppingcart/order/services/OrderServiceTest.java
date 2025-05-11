package com.shoppingcart.order.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shoppingcart.order.clients.InventoryClient;
import com.shoppingcart.order.clients.KafkaEventPublisherClient;
import com.shoppingcart.order.constants.Entity;
import com.shoppingcart.order.dtos.request.OrderItemDTO;
import com.shoppingcart.order.dtos.response.PlaceOrderResponseDTO;
import com.shoppingcart.order.entities.Order;
import com.shoppingcart.order.enums.StatusCode;
import com.shoppingcart.order.messages.EntityStatusMessageFactory;
import com.shoppingcart.order.messages.OrderStatusMessage;
import com.shoppingcart.order.repositories.OrderRepository;
import com.shoppingcart.order.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author Jigs
 */
@ExtendWith({MockitoExtension.class})
public class OrderServiceTest {

    @Mock
    private EntityStatusMessageFactory entityStatusMessageFactory;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private KafkaEventPublisherClient kafkaEventPublisherClient;

    private OrderServiceImpl orderService;

    private static OrderStatusMessage orderStatusMessage;

    @BeforeAll
    static void beforeAll() {
        orderStatusMessage = new OrderStatusMessage();
    }

    @BeforeEach
    void setUp() {
        when(entityStatusMessageFactory.getEntityStatusMessage(Entity.ORDER)).thenReturn(orderStatusMessage);
        orderService = new OrderServiceImpl(
                orderRepository,
                inventoryClient,
                entityStatusMessageFactory,
                kafkaEventPublisherClient
        );
    }

    // successfully save order
    @Test
    @DisplayName("should successfully save order")
    void save_shouldSuccessfullySaveOrder() throws JsonProcessingException {
        // given
        Order order = new Order();
        List<OrderItemDTO> orderItems = List.of(new OrderItemDTO(1L, 5), new OrderItemDTO(2L, 3));
        order.setOrderItems(orderItems);

        // when
        Order savedOrder = new Order(order);
        savedOrder.setId(1L);
        savedOrder.setStatus(StatusCode.PENDING);
        when(orderRepository.save(order)).thenReturn(savedOrder);

        // action
        PlaceOrderResponseDTO response = orderService.save(order);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isNotNull();
        assertThat(response.getStatus().getCode()).isEqualTo(StatusCode.PENDING);
        assertThat(response.getStatus().getMessage()).isEqualTo(orderStatusMessage.getMessage(StatusCode.PENDING));
    }
}
