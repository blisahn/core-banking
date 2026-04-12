package com.devblo.api.controller.admin;

import com.devblo.admin.command.CreateUserCommand;
import com.devblo.admin.query.getAllCustomers.GetAllCustomersQuery;
import com.devblo.admin.query.getAllTransactions.GetAllTransactionsQuery;
import com.devblo.admin.query.getAllUsers.GetAllUsersQuery;
import com.devblo.admin.query.getAuditEvents.GetAuditEventsQuery;
import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.admin.CreateStaffUserRequest;
import com.devblo.audit.AuditEventSummary;
import com.devblo.common.Mediator;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.infrastructure.events.sse.SseEmitterManager;
import com.devblo.transaction.repository.TransactionSummary;
import com.devblo.user.repository.UserSummary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController extends BaseController {

    private final Mediator mediator;
    private final SseEmitterManager sseEmitterManager;

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


    // ─── Audit: Events with SSE ─────────────────────────────────────────
    @GetMapping("/events")
    public ResponseEntity<ApiResponse<PagedResult<AuditEventSummary>>> getAuditEvents(@RequestParam(required = false) String aggregateType,
                                                                                      @RequestParam(required = false) String severity,
                                                                                      @RequestParam(required = false) Instant from,
                                                                                      @RequestParam(required = false) Instant to,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "20") int size) {
        Result<PagedResult<AuditEventSummary>> summaries = mediator.sendQuery(
                new GetAuditEventsQuery(
                        aggregateType, severity, from, to, page, size
                ));
        return respond(summaries);
    }

    @GetMapping(value = "/events/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        return sseEmitterManager.create(30 * 60 * 1000L);  // 30 dk timeout
    }
}
