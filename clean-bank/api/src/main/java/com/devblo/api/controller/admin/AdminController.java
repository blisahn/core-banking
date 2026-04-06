package com.devblo.api.controller.admin;

import com.devblo.admin.command.CreateUserCommand;
import com.devblo.admin.query.getAllCustomers.GetAllCustomersQuery;
import com.devblo.admin.query.getAllTransactions.GetAllTransactionsQuery;
import com.devblo.admin.query.getAllUsers.GetAllUsersQuery;
import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.admin.CreateStaffUserRequest;
import com.devblo.common.Mediator;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.transaction.repository.TransactionSummary;
import com.devblo.user.repository.UserSummary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController extends BaseController {

    private final Mediator mediator;

    // ─── Staff User Management ───────────────────────────────────────

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<Void>> createStaffUser(@RequestBody @Valid CreateStaffUserRequest request) {
        Result<Void> res = mediator.sendCommand(new CreateUserCommand(
                request.email(),
                request.password(),
                request.role()));
        return respond(res);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserSummary>>> getAllUsers() {
        Result<List<UserSummary>> result = mediator.sendQuery(new GetAllUsersQuery());
        return respond(result);
    }

    // ─── Audit: Customers ────────────────────────────────────────────

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<CustomerSummary>>> getAllCustomers() {
        Result<List<CustomerSummary>> result = mediator.sendQuery(new GetAllCustomersQuery());
        return respond(result);
    }

    // ─── Audit: Transactions ─────────────────────────────────────────

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<PagedResult<TransactionSummary>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Result<PagedResult<TransactionSummary>> result = mediator.sendQuery(
                new GetAllTransactionsQuery(page, size));
        return respond(result);
    }
}
