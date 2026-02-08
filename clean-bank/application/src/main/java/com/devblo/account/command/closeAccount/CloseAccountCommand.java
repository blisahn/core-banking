package com.devblo.account.command.closeAccount;

import com.devblo.account.AccountId;
import com.devblo.common.ICommand;
import com.devblo.common.Result;

public record CloseAccountCommand(AccountId accountId) implements ICommand<Result<Boolean>> {
}
