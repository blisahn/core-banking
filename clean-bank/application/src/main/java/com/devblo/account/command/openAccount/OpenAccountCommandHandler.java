package com.devblo.account.command.openAccount;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.AccountNumber;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
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
        var customerExists = customerWriteRepository.findById(cmd.customerId());
        if (customerExists.isEmpty()) {
            return Result.notFound("Customer with id " + cmd.customerId() + " not found");
        } else {
            try {
                AccountNumber accountNumber = AccountNumber.generate();
                Account account = Account.open(
                        accountNumber,
                        cmd.customerId(),
                        cmd.accountType(),
                        Currency.getInstance(cmd.currency())
                );
                accountWriteRepository.save(account);
                return Result.created(account.getId());
            } catch (RuntimeException e) {
                return Result.unexpected(e.getMessage());
            }
        }
    }
}
