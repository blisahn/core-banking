package com.devblo.api.controller;

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
import com.devblo.account.query.getAccaountBalance.GetAccountBalanceQuery;
import com.devblo.account.repository.AccountSummary;
import com.devblo.common.Mediator;
import com.devblo.common.result.Result;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController extends BaseController {

    private final Mediator mediator;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountId>> openAccount(
            @RequestBody @Valid OpenAccountRequest request) {
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
        Result<Void> result = mediator.sendCommand(new DepositMoneyCommand(
                id, request.amount(), request.currency(), request.description()));
        return respond(result);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @PathVariable UUID id,
            @RequestBody @Valid MoneyRequest request) {
        Result<Void> result = mediator.sendCommand(new WithdrawMoneyCommand(
                id, request.amount(), request.currency(), request.description()));
        return respond(result);
    }

    @PostMapping("/{sourceId}/transfer")
    public ResponseEntity<ApiResponse<Void>> transfer(
            @PathVariable UUID sourceId,
            @RequestBody @Valid TransferRequest request) {
        Result<Void> result = mediator.sendCommand(new TransferMoneyCommand(
                sourceId,
                request.targetAccountId(),
                request.amount(),
                request.currency(),
                request.description()));
        return respond(result);
    }

    @PatchMapping("/{id}/freeze")
    public ResponseEntity<ApiResponse<Void>> freeze(@PathVariable UUID id) {
        Result<Void> result = mediator.sendCommand(new FreezeAccountCommand(id));
        return respond(result);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable UUID id) {
        Result<Void> result = mediator.sendCommand(new ActivateAccountCommand(id));
        return respond(result);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<Void>> close(@PathVariable UUID id) {
        Result<Void> result = mediator.sendCommand(new CloseAccountCommand(id));
        return respond(result);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponse<AccountSummary>> getBalance(@PathVariable UUID id) {
        Result<AccountSummary> result = mediator.sendQuery(new GetAccountBalanceQuery(id));
        return respond(result);
    }
}
