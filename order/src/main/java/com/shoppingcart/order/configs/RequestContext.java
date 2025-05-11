package com.shoppingcart.order.configs;

/**
 * @author Jigs
 */
public class RequestContext {

    private RequestContext() {
        // Prevent instantiation
    }
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
