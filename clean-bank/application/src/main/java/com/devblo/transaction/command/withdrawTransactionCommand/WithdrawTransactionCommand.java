package com.devblo.transaction.command.withdrawTransactionCommand;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.transaction.TransactionId;

import java.math.BigDecimal;

public record WithdrawTransactionCommand(String sourceAccountId, BigDecimal amount, String currency,
                                         String description) implements ICommand<Result<TransactionId>> {
}
