package com.devblo.api.controller;

import com.devblo.api.controller.common.ApiResponse;
import com.devblo.api.controller.common.BaseController;
import com.devblo.api.model.request.DepositTransactionRequest;
import com.devblo.api.model.request.TransferTransactionRequest;
import com.devblo.api.model.request.WithdrawTransactionRequest;
import com.devblo.common.Mediator;
import com.devblo.transaction.TransactionId;
import com.devblo.transaction.command.depositTransactionCommand.DepositTransactionCommand;
import com.devblo.transaction.command.transferTransacitonCommand.TransferTransactionCommand;
import com.devblo.transaction.command.withdrawTransactionCommand.WithdrawTransactionCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions/")
@RequiredArgsConstructor
public class TransactionController extends BaseController {

    private final Mediator mediator;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionId>> deposit(@RequestBody @Valid DepositTransactionRequest depositTransactionRequest) {
        var res = mediator.sendCommand(new DepositTransactionCommand(
                depositTransactionRequest.targetAccountId(),
                depositTransactionRequest.amount(),
                depositTransactionRequest.currency(),
                depositTransactionRequest.description()
        ));
        return respond(res);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionId>> withdraw(@RequestBody @Valid WithdrawTransactionRequest withdrawTransactionRequest) {
        var res = mediator.sendCommand(new WithdrawTransactionCommand(
                withdrawTransactionRequest.sourceAccountId(),
                withdrawTransactionRequest.amount(),
                withdrawTransactionRequest.currency(),
                withdrawTransactionRequest.description()
        ));
        return respond(res);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionId>> transfer(@RequestBody @Valid TransferTransactionRequest transferTransactionRequest) {
        var res = mediator.sendCommand(new TransferTransactionCommand(
                transferTransactionRequest.sourceAccountId(),
                transferTransactionRequest.targetAccountId(),
                transferTransactionRequest.currency(),
                transferTransactionRequest.amount(),
                transferTransactionRequest.description()
        ));
        return respond(res);
    }
}
