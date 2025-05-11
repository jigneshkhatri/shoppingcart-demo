package com.shoppingcart.order.messages;

import com.shoppingcart.order.constants.Entity;
import com.shoppingcart.order.exceptions.InvalidEntityException;
import org.springframework.stereotype.Component;

/**
 * @author Jigs
 */
@Component
public class EntityStatusMessageFactory {

    private OrderStatusMessage orderStatusMessage;

    public EntityStatusMessageFactory() {
        this.orderStatusMessage = new OrderStatusMessage();
    }

    public AbstractEntityStatusMessage getEntityStatusMessage(String entityType) {
        if (entityType.equalsIgnoreCase(Entity.ORDER)) {
            return orderStatusMessage;
        }

        throw new InvalidEntityException("Invalid entity type: " + entityType);
    }

}
