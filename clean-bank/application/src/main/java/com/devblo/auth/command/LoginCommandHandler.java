package com.devblo.auth.command;

import com.devblo.auth.dto.AuthTokenDto;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.common.security.PasswordEncoderPort;
import com.devblo.common.security.TokenGeneratorPort;
import com.devblo.user.repository.IUserWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginCommandHandler implements ICommandHandler<LoginCommand, Result<AuthTokenDto>> {

    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenGeneratorPort tokenGeneratorPort;
    private final IUserWriteRepository userWriteRepository;

    @Override
    public Result<AuthTokenDto> handle(LoginCommand command) {

        var userOpt = userWriteRepository.findByEmail(command.email());
        if (userOpt.isEmpty()) {
            return Result.failure("Invalid email or password. ");
        }
        var user = userOpt.get();

        if (!passwordEncoderPort.matches(command.password(), user.getPassword())) {
            return Result.failure("Invalid email or password. ");
        }

        String customerId = user.getCustomerId() != null
                ? user.getCustomerId().value().toString()
                : null;

        String token = tokenGeneratorPort.generateToken(
                user.getId().value().toString(),
                user.getEmail().value(),
                user.getRole().toString(),
                customerId
        );
        return Result.success(new AuthTokenDto(token, 3600L));
    }
}