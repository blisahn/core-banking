package com.devblo.account.command.activateAccount;

import com.devblo.account.AccountId;
import com.devblo.common.ICommand;
import com.devblo.common.Result;

public record ActivateAccountCommand(AccountId accountId) implements ICommand<Result<Boolean>> {
}
