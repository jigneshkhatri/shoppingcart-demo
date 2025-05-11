package com.shoppingcart.inventory.configs;

import com.shoppingcart.inventory.exceptions.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * @author Jigs
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    // define open API paths and their corresponding request methods
    private static final Map<String, String> OPEN_API_PATHS = Map.of();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (checkForOpenApi(request)) return true; // Allow access to open API paths

        String userIdStr = request.getHeader("X-User-Id");
        if (userIdStr == null || userIdStr.isEmpty()) {
            throw new AuthException("User ID is missing in the request header");
        }
        RequestContext.setUserId(Long.parseLong(userIdStr));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        RequestContext.clear();
    }

    private static boolean checkForOpenApi(HttpServletRequest request) {
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String requestPath = requestURI.substring(requestURI.indexOf("/inventory-service/"));
        String requestPathMethod = OPEN_API_PATHS.get(requestPath);
        if (requestPathMethod == null || !requestPathMethod.equals(requestMethod)) {
            return true;
        }
        return false;
    }
}
