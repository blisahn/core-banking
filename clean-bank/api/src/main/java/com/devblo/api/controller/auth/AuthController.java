package com.devblo.api.controller.auth;

import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.auth.LoginRequest;
import com.devblo.api.model.request.auth.RegisterRequest;
import com.devblo.auth.command.LoginCommand;
import com.devblo.auth.dto.AuthTokenDto;
import com.devblo.common.Mediator;
import com.devblo.common.result.Result;
import com.devblo.customer.command.registerCustomer.RegisterCustomerCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final Mediator mediator;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenDto>> login(@RequestBody @Valid LoginRequest request) {
        Result<AuthTokenDto> res = mediator.sendCommand(new LoginCommand(
                request.email(), request.password()
        ));
        return respond(res);
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequest request) {
        var res = mediator.sendCommand(new RegisterCustomerCommand(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.dateOfBirth(),
                request.street(),
                request.district(),
                request.password()));

        return respond(res);
    }


}
