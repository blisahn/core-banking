package com.devblo.api.controller.common;

import com.devblo.common.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.UUID;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> respond(Result<T> result) {
        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(result.getValue()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure(result.getError(), result.getError()));
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getPrincipalDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Map<String, String>) authentication.getPrincipal();
    }

    protected UUID getAuthenticatedUserId() {
        return UUID.fromString(getPrincipalDetails().get("userId"));
    }

    protected UUID getAuthenticatedCustomerId() {
        String customerId = getPrincipalDetails().get("customerId");
        if (customerId == null) {
            return null;
        }
        return UUID.fromString(customerId);
    }

    protected boolean isAdminOrEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_EMPLOYEE"));
    }

    protected <T> ResponseEntity<ApiResponse<T>> forbidden() {
        return ResponseEntity.status(403)
                .body(ApiResponse.failure("Access denied", "FORBIDDEN"));
    }
}

