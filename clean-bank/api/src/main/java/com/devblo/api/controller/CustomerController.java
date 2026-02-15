package com.devblo.api.controller;

import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.common.Mediator;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.command.registerCustomer.RegisterCustomerCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController extends BaseController {

    private final Mediator mediator;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerId>> register(@RequestBody String firstName, String LastName, String email,
                                                            String dateOfBirth, String street, String district) {
        var result = mediator.sendCommand(new RegisterCustomerCommand(firstName, LastName, email, dateOfBirth, street, district));
        return respond(result);
    }

    @PutMapping("/{id}/personal-info")
    public ResponseEntity<ApiResponse<PersonalInfo>> updatePersonalInfo() {

    }
}
