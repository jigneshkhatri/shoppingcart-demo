package com.shoppingcart.order.clients;

import com.shoppingcart.order.dtos.request.OrderItemDTO;
import com.shoppingcart.order.exceptions.InventoryServiceException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Jigs
 */
@Component
public class InventoryClient {

    @Value("${inventory.service.base.url}")
    private String inventoryServiceBaseUrl;

    @Value("${inventory.service.saveOrderItems.endpoint}")
    private String saveOrderItemsEndpoint;

    private String saveOrderItemsFullEndpoint;

    private final RestTemplate restTemplate;

    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        this.saveOrderItemsFullEndpoint = inventoryServiceBaseUrl + saveOrderItemsEndpoint;
    }

    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void saveOrderItems(Long orderId, List<OrderItemDTO> orderItems) {
        String url = this.saveOrderItemsFullEndpoint.replaceAll("\\{orderId}", String.valueOf(orderId));
        try {
            restTemplate.postForEntity(url, orderItems, Void.class);
        } catch (HttpStatusCodeException e) {
            // Extract details from response
            throw new InventoryServiceException("Inventory service responded with error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new InventoryServiceException("Failed to call Inventory service", e);
        }
    }
}
