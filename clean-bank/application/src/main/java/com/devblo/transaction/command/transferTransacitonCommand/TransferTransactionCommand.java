package com.devblo.transaction.command.transferTransacitonCommand;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.transaction.TransactionId;

import java.math.BigDecimal;

public record TransferTransactionCommand(String sourceAccountId, String targetAccountId, String currency, BigDecimal amount,
                                         String description) implements ICommand<Result<TransactionId>> {
}
