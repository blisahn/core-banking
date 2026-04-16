package com.devblo.customer.command.registerCustomer;

import com.devblo.account.Account;
import com.devblo.account.AccountNumber;
import com.devblo.account.AccountType;
import com.devblo.account.repository.IAccountWriteRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;

@Service
public class RegisterCustomerCommandHandler implements ICommandHandler<RegisterCustomerCommand, Result<Void>> {

    private final ICustomerWriteRepository customerWriteRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final IUserWriteRepository userWriteRepository;
    private final IAccountWriteRepository accountWriteRepository;

    public RegisterCustomerCommandHandler(ICustomerWriteRepository customerWriteRepository,
                                          PasswordEncoderPort passwordEncoderPort,
                                          IUserWriteRepository userWriteRepository,
                                          IAccountWriteRepository accountWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.userWriteRepository = userWriteRepository;
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(RegisterCustomerCommand command) {
        boolean isEmailExists = customerWriteRepository.findByEmail(command.email()).isPresent();
        if (isEmailExists) {
            return Result.failure("Customer with email " + command.email() + " already exists");
        }

        String encodedPassword = passwordEncoderPort.encode(command.password());

        var personalInfo = PersonalInfo.of(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.dateOfBirth(),
                encodedPassword
        );

        Address address = Address.of(command.street(), command.district());
        var customer = Customer.register(personalInfo, address);
        var user = User.create(
                Email.of(command.email()),
                encodedPassword,
                Role.CUSTOMER,
                customer.getId()
        );
        customerWriteRepository.save(customer);
        userWriteRepository.save(user);

        // Open a default CHECKING account with TRY currency
        Account defaultAccount = Account.open(
                AccountNumber.generate(),
                customer.getId(),
                AccountType.CHECKING,
                Currency.getInstance("TRY")
        );
        accountWriteRepository.save(defaultAccount);

        return Result.success();
    }
}
