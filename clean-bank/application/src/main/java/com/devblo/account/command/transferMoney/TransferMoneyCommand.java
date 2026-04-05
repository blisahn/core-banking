package com.devblo.account.command.transferMoney;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferMoneyCommand(
        UUID sourceAccountId,
        String targetAccountNumber,
        BigDecimal amount,
        String currency,
        String description) implements ICommand<Result<Void>> {
}
