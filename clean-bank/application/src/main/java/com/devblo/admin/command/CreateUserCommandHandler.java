package com.devblo.admin.command;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.common.security.PasswordEncoderPort;
import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.repository.ICustomerWriteRepository;
import com.devblo.shared.Email;
import com.devblo.user.Role;
import com.devblo.user.User;
import com.devblo.user.repository.IUserWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateUserCommandHandler implements ICommandHandler<CreateUserCommand, Result<Void>> {

    private final IUserWriteRepository userWriteRepository;
    private final ICustomerWriteRepository customerWriteRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional
    public Result<Void> handle(CreateUserCommand command) {
        var userOpt = userWriteRepository.findByEmail(command.email());
        if (userOpt.isPresent()) {
            return Result.failure(String.format("User with email: %s already exists", command.email()));
        }

        Role role;
        try {
            role = Role.valueOf(command.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Result.failure("Invalid role: " + command.role() + ". Must be ADMIN or EMPLOYEE.");
        }

        if (role == Role.CUSTOMER) {
            return Result.failure("Cannot create CUSTOMER via admin endpoint. Use /api/auth/register or /api/employees/customers.");
        }

        // Generate dummy customer data for Staff
        var personalInfo = PersonalInfo.of(
                "Staff",
                "Member",
                command.email(),
                LocalDate.of(1990, 1, 1),
                passwordEncoderPort.encode(command.password())
        );

        Address address = Address.of("System Street", "System District");
        var customer = Customer.register(personalInfo, address);
        customerWriteRepository.save(customer);

        var user = User.create(
                Email.of(command.email()),
                passwordEncoderPort.encode(command.password()),
                role,
                customer.getId()
        );
        userWriteRepository.save(user);
        
        return Result.success();
    }
}
