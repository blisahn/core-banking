package com.devblo.account.command.withdrawMoney;

import com.devblo.common.ICommand;
import com.devblo.common.Result;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawMoneyCommand(UUID id, BigDecimal amount, String currency) implements ICommand<Result<Void>> {
}
