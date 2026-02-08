package com.devblo.account.command.depositMoney;

import com.devblo.account.AccountId;
import com.devblo.common.ICommand;
import com.devblo.common.Result;
import com.devblo.shared.Money;

public record DepositMoneyCommand(AccountId id, Money money) implements ICommand<Result<Void>> {
}
