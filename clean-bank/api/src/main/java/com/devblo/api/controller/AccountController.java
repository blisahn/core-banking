package com.devblo.api.controller;

import com.devblo.api.config.security.AccountOwnershipService;
import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.account.MoneyRequest;
import com.devblo.api.model.request.account.OpenAccountRequest;
import com.devblo.api.model.request.account.TransferRequest;
import com.devblo.account.AccountId;
import com.devblo.account.command.activateAccount.ActivateAccountCommand;
import com.devblo.account.command.closeAccount.CloseAccountCommand;
import com.devblo.account.command.depositMoney.DepositMoneyCommand;
import com.devblo.account.command.freezeAccount.FreezeAccountCommand;
import com.devblo.account.command.openAccount.OpenAccountCommand;
import com.devblo.account.command.transferMoney.TransferMoneyCommand;
import com.devblo.account.command.withdrawMoney.WithdrawMoneyCommand;
import com.devblo.account.query.getAccountBalance.GetAccountBalanceQuery;
import com.devblo.account.query.getAccountTransactions.GetAccountTransactionsQuery;
import com.devblo.account.query.getTransactionStats.GetTransactionStatsQuery;
import com.devblo.account.query.getCustomerAccounts.GetCustomerAccountsQuery;
import com.devblo.account.repository.AccountSummary;
import com.devblo.common.Mediator;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.TransactionStatsSummary;
import com.devblo.transaction.repository.TransactionSummary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController extends BaseController {

    private final Mediator mediator;
    private final AccountOwnershipService ownershipService;

    private boolean isAccountOwner(UUID accountId) {
        return ownershipService.isOwner(accountId, getAuthenticatedCustomerId());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountId>> openAccount(
            @RequestBody @Valid OpenAccountRequest request) {
        if (!getAuthenticatedCustomerId().equals(request.customerId())) {
            return forbidden();
        }
        Result<AccountId> result = mediator.sendCommand(new OpenAccountCommand(
                request.customerId(),
                request.accountType(),
                request.currency()));
        return respond(result);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse<Void>> deposit(
            @PathVariable UUID id,
            @RequestBody @Valid MoneyRequest request) {
        if (!isAccountOwner(id)) return forbidden();
        Result<Void> result = mediator.sendCommand(new DepositMoneyCommand(
                id, request.amount(), request.currency(), request.description()));
        return respond(result);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable UUID id,
            @RequestBody @Valid MoneyRequest request) {
        if (!isAccountOwner(id)) return forbidden();
        Result<Void> result = mediator.sendCommand(new WithdrawMoneyCommand(
                id, request.amount(), request.currency(), request.description()));
        return respond(result);
    }

    @PostMapping("/{sourceId}/transfer")
    public ResponseEntity<ApiResponse<Void>> transfer(
            @PathVariable UUID sourceId,
            @RequestBody @Valid TransferRequest request) {
        if (!isAccountOwner(sourceId)) return forbidden();
        Result<Void> result = mediator.sendCommand(new TransferMoneyCommand(
                sourceId,
                request.targetAccountNumber(),
                request.amount(),
                request.currency(),
                request.description()));
        return respond(result);
    }

    @PatchMapping("/{id}/freeze")
    public ResponseEntity<ApiResponse<Void>> freeze(@PathVariable UUID id) {
        if (!isAccountOwner(id)) return forbidden();
        Result<Void> result = mediator.sendCommand(new FreezeAccountCommand(id));
        return respond(result);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable UUID id) {
        if (!isAccountOwner(id)) return forbidden();
        Result<Void> result = mediator.sendCommand(new ActivateAccountCommand(id));
        return respond(result);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<Void>> close(@PathVariable UUID id) {
        if (!isAccountOwner(id)) return forbidden();
        Result<Void> result = mediator.sendCommand(new CloseAccountCommand(id));
        return respond(result);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountSummary>>> getMyAccounts() {
        Result<List<AccountSummary>> result = mediator.sendQuery(new GetCustomerAccountsQuery(getAuthenticatedCustomerId()));
        return respond(result);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponse<AccountSummary>> getBalance(@PathVariable UUID id) {
        if (!isAccountOwner(id)) return forbidden();
        Result<AccountSummary> result = mediator.sendQuery(new GetAccountBalanceQuery(id));
        return respond(result);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<ApiResponse<PagedResult<TransactionSummary>>> getTransactions(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        if (!isAccountOwner(id)) return forbidden();
        Instant fromDate = from != null ? from : Instant.EPOCH;
        Instant toDate = to != null ? to : Instant.now();
        Result<PagedResult<TransactionSummary>> result = mediator.sendQuery(
                new GetAccountTransactionsQuery(id, fromDate, toDate, page, size));
        return respond(result);
    }

    @GetMapping("/{id}/transactions/stats")
    public ResponseEntity<ApiResponse<TransactionStatsSummary>> getTransactionStats(
            @PathVariable UUID id,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        if (!isAccountOwner(id)) return forbidden();
        Instant fromDate = from != null ? from : Instant.EPOCH;
        Instant toDate = to != null ? to : Instant.now();
        Result<TransactionStatsSummary> result = mediator.sendQuery(
                new GetTransactionStatsQuery(id, fromDate, toDate));
        return respond(result);
    }
}
