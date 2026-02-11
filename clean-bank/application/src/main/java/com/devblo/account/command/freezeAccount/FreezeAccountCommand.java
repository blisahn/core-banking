package com.devblo.account.command.freezeAccount;

import com.devblo.common.ICommand;
import com.devblo.common.Result;

import java.util.UUID;

public record FreezeAccountCommand(UUID accountId) implements ICommand<Result<Void>> {
}
