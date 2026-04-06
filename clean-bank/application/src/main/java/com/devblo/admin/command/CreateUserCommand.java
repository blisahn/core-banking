package com.devblo.admin.command;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

public record CreateUserCommand(
        String email,
        String password,
        String role
) implements ICommand<Result<Void>> {
}
