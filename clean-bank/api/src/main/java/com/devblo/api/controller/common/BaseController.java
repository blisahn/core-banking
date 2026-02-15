package com.devblo.api.controller.common;

import com.devblo.common.result.Result;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> respond(Result<T> result) {
        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(result.getValue()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure(result.getError()));
        }
    }
}
