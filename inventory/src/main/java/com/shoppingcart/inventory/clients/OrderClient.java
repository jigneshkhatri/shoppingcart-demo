package com.shoppingcart.inventory.clients;

import com.shoppingcart.inventory.enums.StatusCode;
import com.shoppingcart.inventory.exceptions.OrderServiceException;
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
public class OrderClient {
    @Value("${order.service.base.url}")
    private String orderServiceBaseUrl;

    @Value("${order.service.lockOrder.endpoint}")
    private String lockOrderEndpoint;

    @Value("${order.service.unlockOrder.endpoint}")
    private String unlockOrderEndpoint;

    private String lockOrderFullEndpoint;

    private String unlockOrderFullEndpoint;

    private final RestTemplate restTemplate;

    public OrderClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        this.lockOrderFullEndpoint = orderServiceBaseUrl + lockOrderEndpoint;
        this.unlockOrderFullEndpoint = orderServiceBaseUrl + unlockOrderEndpoint;
    }

    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void lockOrder(Long orderId, String transactionId) {
        String url = this.lockOrderFullEndpoint.replaceAll("\\{orderId}", String.valueOf(orderId)) + "?transactionId=" + transactionId;
        try {
            restTemplate.getForEntity(url, Void.class);
        } catch (HttpStatusCodeException e) {
            // Extract details from response
            throw new OrderServiceException("Order service responded with error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new OrderServiceException("Failed to call Order service", e);
        }
    }

    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void unlockOrderAndUpdateStatus(Long orderId, StatusCode status, String transactionId) {
        String url = this.unlockOrderFullEndpoint.replaceAll("\\{orderId}", String.valueOf(orderId)) + "?transactionId=" + transactionId + "&status=" + status;
        try {
            restTemplate.getForEntity(url, Void.class);
        } catch (HttpStatusCodeException e) {
            // Extract details from response
            throw new OrderServiceException("Order service responded with error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new OrderServiceException("Failed to call Order service", e);
        }
    }
}
