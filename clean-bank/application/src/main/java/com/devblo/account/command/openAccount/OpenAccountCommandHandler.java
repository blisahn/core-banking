package com.devblo.account.command.openAccount;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.AccountNumber;
import com.devblo.account.AccountType;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;

@Service
public class OpenAccountCommandHandler
        implements ICommandHandler<OpenAccountCommand, Result<AccountId>> {

    private final IAccountWriteRepository accountWriteRepository;
    private final ICustomerWriteRepository customerWriteRepository;

    public OpenAccountCommandHandler(IAccountWriteRepository accountWriteRepository, ICustomerWriteRepository customerWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<AccountId> handle(OpenAccountCommand cmd) {
        var customerExists = customerWriteRepository.findById(CustomerId.of(cmd.customerId()));
        if (customerExists.isEmpty()) {
            return Result.failure("Customer with id " + cmd.customerId() + " not found");
        } else {
            try {
                AccountNumber accountNumber = AccountNumber.generate();
                Account account = Account.open(
                        accountNumber,
                        CustomerId.of(cmd.customerId()),
                        AccountType.valueOf(cmd.accountType().toUpperCase()),
                        Currency.getInstance(cmd.currency())
                );
                accountWriteRepository.save(account);
                return Result.success(account.getId());
            } catch (RuntimeException e) {
                return Result.failure(e.getMessage());
            }
        }
    }
}
