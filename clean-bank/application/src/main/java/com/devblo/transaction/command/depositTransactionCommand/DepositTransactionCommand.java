package com.devblo.transaction.command.depositTransactionCommand;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.transaction.TransactionId;

import java.math.BigDecimal;

public record DepositTransactionCommand(String targetAccountId, BigDecimal amount,
                                        String currency, String description) implements ICommand<Result<TransactionId>> {
}
