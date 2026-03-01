package com.devblo.account.command.transferMoney;

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
public class TransferMoneyCommandHandler implements ICommandHandler<TransferMoneyCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;
    private final ITransactionWriteRepository transactionWriteRepository;

    public TransferMoneyCommandHandler(IAccountWriteRepository accountWriteRepository,
            ITransactionWriteRepository transactionWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(TransferMoneyCommand command) {
        AccountId sourceId = AccountId.of(command.sourceAccountId());
        AccountId targetId = AccountId.of(command.targetAccountId());
        Money amount = Money.of(command.amount(), command.currency());

        // Find source account
        Result<Account> sourceResult = accountWriteRepository
                .findById(sourceId)
                .map(Result::success)
                .orElseGet(() -> Result.failure("Source account not found"));
        if (sourceResult.isFailure()) {
            return Result.failure(sourceResult.getError());
        }

        // Find target account
        Result<Account> targetResult = accountWriteRepository
                .findById(targetId)
                .map(Result::success)
                .orElseGet(() -> Result.failure("Target account not found"));
        if (targetResult.isFailure()) {
            return Result.failure(targetResult.getError());
        }

        Account sourceAccount = sourceResult.getValue();
        Account targetAccount = targetResult.getValue();

        // Withdraw from source
        Result<Void> withdrawResult = sourceAccount.withdraw(amount);
        if (withdrawResult.isFailure()) {
            return withdrawResult;
        }

        // Deposit to target
        Result<Void> depositResult = targetAccount.deposit(amount);
        if (depositResult.isFailure()) {
            return depositResult;
        }

        // Transaction audit log
        Result<Transaction> txResult = Transaction.transfer(
                sourceId, targetId, amount, command.description());
        if (txResult.isFailure()) {
            return Result.failure(txResult.getError());
        }

        accountWriteRepository.save(sourceAccount);
        accountWriteRepository.save(targetAccount);
        transactionWriteRepository.save(txResult.getValue());
        return Result.success();
    }
}
