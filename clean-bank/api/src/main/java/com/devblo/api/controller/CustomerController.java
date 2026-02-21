package com.devblo.api.controller;

import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.customer.RegisterCustomerRequest;
import com.devblo.api.model.request.customer.UpdateAddressRequest;
import com.devblo.api.model.request.customer.UpdatePersonalInfoRequest;
import com.devblo.common.Mediator;
import com.devblo.common.result.Result;
import com.devblo.customer.Address;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.command.activateCustomer.ActivateCustomerCommand;
import com.devblo.customer.command.closeCustomer.CloseCustomerCommand;
import com.devblo.customer.command.registerCustomer.RegisterCustomerCommand;
import com.devblo.customer.command.suspendCustomer.SuspendCustomerCommand;
import com.devblo.customer.command.updateAddress.UpdateAddressCommand;
import com.devblo.customer.command.updatePersonalInfo.UpdatePersonalInfoCommand;
import com.devblo.customer.query.getCustomerSummary.GetCustomerSummaryQuery;
import com.devblo.customer.repository.CustomerSummary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController extends BaseController {

    private final Mediator mediator;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerId>> register(@RequestBody @Valid RegisterCustomerRequest command) {
        Result<CustomerId> result = mediator.sendCommand(new RegisterCustomerCommand(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.dateOfBirth(),
                command.street(),
                command.district()));
        return respond(result);
    }

    @PutMapping("/{id}/personal-info")
    public ResponseEntity<ApiResponse<PersonalInfo>> updatePersonalInfo(
            @PathVariable String id,
            @RequestBody @Valid UpdatePersonalInfoRequest request) {
        Result<PersonalInfo> result = mediator.sendCommand(new UpdatePersonalInfoCommand(
                id,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.dateOfBirth()));
        return respond(result);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<ApiResponse<Address>> updateAddress(
            @PathVariable String id,
            @RequestBody @Valid UpdateAddressRequest request) {
        Result<Address> result = mediator.sendCommand(new UpdateAddressCommand(
                id,
                request.street(),
                request.district()));
        return respond(result);
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspend(@PathVariable String id) {
        Result<Void> result = mediator.sendCommand(new SuspendCustomerCommand(id));
        return respond(result);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable String id) {
        Result<Void> result = mediator.sendCommand(new ActivateCustomerCommand(id));
        return respond(result);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<Void>> close(@PathVariable String id) {
        Result<Void> result = mediator.sendCommand(new CloseCustomerCommand(id));
        return respond(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerSummary>> getCustomer(@PathVariable String id) {
        Result<CustomerSummary> result = mediator.sendQuery(new GetCustomerSummaryQuery(id));
        return respond(result);
    }

}
