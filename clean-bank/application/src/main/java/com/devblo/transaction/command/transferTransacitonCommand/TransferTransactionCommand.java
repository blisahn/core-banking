package com.devblo.transaction.command.transferTransacitonCommand;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.transaction.TransactionId;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferTransactionCommand(UUID sourceAccountId, UUID targetAccountId, String currency, BigDecimal amount,
                                         String description) implements ICommand<Result<TransactionId>> {
}
