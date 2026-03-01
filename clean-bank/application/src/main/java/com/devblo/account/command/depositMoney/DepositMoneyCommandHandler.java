package com.devblo.account.command.depositMoney;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositMoneyCommandHandler implements ICommandHandler<DepositMoneyCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;
    private final ITransactionWriteRepository transactionWriteRepository;

    public DepositMoneyCommandHandler(IAccountWriteRepository accountWriteRepository,
            ITransactionWriteRepository transactionWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(DepositMoneyCommand cmd) {
        Result<Account> optAccount = accountWriteRepository
                .findById(AccountId.of(cmd.id()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
        if (optAccount.isFailure()) {
            return Result.failure(optAccount.getError());
        }

        Account account = optAccount.getValue();
        Money amount = Money.of(cmd.amount(), cmd.currency());

        Result<Void> depositResult = account.deposit(amount);
        if (depositResult.isFailure()) {
            return Result.failure(depositResult.getError());
        }

        // Transaction audit log
        Result<Transaction> txResult = Transaction.deposit(
                AccountId.of(cmd.id()), amount, cmd.description());
        if (txResult.isFailure()) {
            return Result.failure(txResult.getError());
        }

        accountWriteRepository.save(account);
        transactionWriteRepository.save(txResult.getValue());
        return Result.success();
    }
}
