package com.devblo.account.command.depositMoney;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositMoneyCommand(UUID id, BigDecimal amount, String currency) implements ICommand<Result<Void>> {
}
