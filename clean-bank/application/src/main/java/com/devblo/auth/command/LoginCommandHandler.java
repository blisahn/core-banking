package com.devblo.auth.command;

import com.devblo.auth.dto.AuthTokenDto;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.common.security.PasswordEncoderPort;
import com.devblo.common.security.TokenGeneratorPort;
import com.devblo.customer.repository.ICustomerWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginCommandHandler implements ICommandHandler<LoginCommand, Result<AuthTokenDto>> {

    private final ICustomerWriteRepository customerWriteRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenGeneratorPort tokenGeneratorPort;

    @Override
    public Result<AuthTokenDto> handle(LoginCommand command) {
        var customerOpt = customerWriteRepository.findByEmail(command.email());
        if (customerOpt.isEmpty()) {
            return Result.failure("Invalid email or password. ");
        }
        var customer = customerOpt.get();

        if (!passwordEncoderPort.matches(command.password(), customer.getPersonalInfo().password())) {
            return Result.failure("Invalid email or password. ");
        }

        String token = tokenGeneratorPort.generateToken(
                customer.getId().value().toString(),
                customer.getPersonalInfo().email().value(),
                "USER"
        );
        return Result.success(new AuthTokenDto(token, 3600L));
    }
}
