package com.devblo.api.controller.employee;

import com.devblo.account.command.activateAccount.ActivateAccountCommand;
import com.devblo.account.command.closeAccount.CloseAccountCommand;
import com.devblo.account.command.freezeAccount.FreezeAccountCommand;
import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.auth.RegisterRequest;
import com.devblo.common.Mediator;
import com.devblo.common.result.Result;
import com.devblo.customer.command.activateCustomer.ActivateCustomerCommand;
import com.devblo.customer.command.closeCustomer.CloseCustomerCommand;
import com.devblo.customer.command.registerCustomer.RegisterCustomerCommand;
import com.devblo.customer.command.suspendCustomer.SuspendCustomerCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController extends BaseController {

    private final Mediator mediator;

    // ─── Account State Operations (staff-only) ───────────────────────

    @PatchMapping("/accounts/{id}/freeze")
    public ResponseEntity<ApiResponse<Void>> freezeAccount(@PathVariable UUID id) {
        Result<Void> result = mediator.sendCommand(new FreezeAccountCommand(id));
        return respond(result);
    }

    @PatchMapping("/accounts/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateAccount(@PathVariable UUID id) {
        Result<Void> result = mediator.sendCommand(new ActivateAccountCommand(id));
        return respond(result);
    }

    @PatchMapping("/accounts/{id}/close")
    public ResponseEntity<ApiResponse<Void>> closeAccount(@PathVariable UUID id) {
        Result<Void> result = mediator.sendCommand(new CloseAccountCommand(id));
        return respond(result);
    }

    // ─── Customer State Operations (staff-only) ──────────────────────

    @PatchMapping("/customers/{id}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspendCustomer(@PathVariable String id) {
        Result<Void> result = mediator.sendCommand(new SuspendCustomerCommand(id));
        return respond(result);
    }

    @PatchMapping("/customers/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateCustomer(@PathVariable String id) {
        Result<Void> result = mediator.sendCommand(new ActivateCustomerCommand(id));
        return respond(result);
    }

    @PatchMapping("/customers/{id}/close")
    public ResponseEntity<ApiResponse<Void>> closeCustomer(@PathVariable String id) {
        Result<Void> result = mediator.sendCommand(new CloseCustomerCommand(id));
        return respond(result);
    }

    // ─── Customer Registration (on behalf of walk-in customer) ───────

    @PostMapping("/customers")
    public ResponseEntity<ApiResponse<Void>> registerCustomer(@RequestBody @Valid RegisterRequest request) {
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
