package com.devblo.account.command.freezeAccount;

import com.devblo.account.AccountId;
import com.devblo.common.ICommand;
import com.devblo.common.Result;

public record FreezeAccountCommand(AccountId accountId) implements ICommand<Result<Boolean>> {
}
