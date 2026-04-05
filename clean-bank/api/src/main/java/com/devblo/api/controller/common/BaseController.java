package com.devblo.api.controller.common;

import com.devblo.common.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> respond(Result<T> result) {
        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(result.getValue()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure(result.getError(),result.getError()));
        }
    }

    protected UUID getAuthenticatedCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var x = authentication.getPrincipal();
        return UUID.fromString((String)x);
    }

    protected <T> ResponseEntity<ApiResponse<T>> forbidden() {
        return ResponseEntity.status(403)
                .body(ApiResponse.failure("Access denied", "FORBIDDEN"));
    }
}
