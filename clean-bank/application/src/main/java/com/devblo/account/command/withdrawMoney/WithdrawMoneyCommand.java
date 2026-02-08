package com.devblo.account.command.withdrawMoney;

import com.devblo.account.AccountId;
import com.devblo.common.ICommand;
import com.devblo.common.Result;
import com.devblo.shared.Money;

public record WithdrawMoneyCommand(AccountId id, Money money) implements ICommand<Result<Boolean>> {
}
