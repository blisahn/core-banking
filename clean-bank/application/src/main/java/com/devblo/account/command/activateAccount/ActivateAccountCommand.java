package com.devblo.account.command.activateAccount;

import com.devblo.common.ICommand;
import com.devblo.common.Result;

import java.util.UUID;

public record ActivateAccountCommand(UUID accountId) implements ICommand<Result<Void>> {
}
