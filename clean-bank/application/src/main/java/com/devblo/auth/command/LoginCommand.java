package com.devblo.auth.command;

import com.devblo.auth.dto.AuthTokenDto;
import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

public record LoginCommand(String email, String password) implements ICommand<Result<AuthTokenDto>> {
}
