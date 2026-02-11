package com.devblo.account.command.closeAccount;

import com.devblo.common.ICommand;
import com.devblo.common.Result;

import java.util.UUID;

public record CloseAccountCommand(UUID accountId) implements ICommand<Result<Void>> {
}
