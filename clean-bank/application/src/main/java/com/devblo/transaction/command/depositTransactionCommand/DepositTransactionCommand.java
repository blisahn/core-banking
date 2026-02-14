package com.devblo.transaction.command.depositTransactionCommand;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.transaction.TransactionId;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositTransactionCommand(UUID targetAccountId, BigDecimal amount,
                                        String currency, String description) implements ICommand<Result<TransactionId>> {
}
