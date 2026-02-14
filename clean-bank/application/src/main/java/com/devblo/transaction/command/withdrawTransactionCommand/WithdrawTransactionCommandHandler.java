package com.devblo.transaction.command.withdrawTransactionCommand;

import com.devblo.account.AccountId;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.TransactionId;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawTransactionCommandHandler implements ICommandHandler<WithdrawTransactionCommand, Result<TransactionId>> {
    private final ITransactionWriteRepository transactionWriteRepository;

    public WithdrawTransactionCommandHandler(ITransactionWriteRepository transactionWriteRepository) {
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<TransactionId> handle(WithdrawTransactionCommand command) {
        Result<Transaction> transactionResult = Transaction.withdraw(
                AccountId.of(command.sourceAccountId()),
                Money.of(command.amount(), command.currency()),
                command.description()
        );
        if (transactionResult.isFailure()) {
            return Result.failure(transactionResult.getError());
        }
        Transaction transaction = transactionResult.getValue();
        transactionWriteRepository.save(transaction);
        return Result.success(transaction.getId());
    }
}
