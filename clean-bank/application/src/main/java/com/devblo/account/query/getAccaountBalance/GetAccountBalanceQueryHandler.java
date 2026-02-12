package com.devblo.account.query.getAccaountBalance;

import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountReadRepository;
import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAccountBalanceQueryHandler implements IQueryHandler<GetAccountBalanceQuery, Result<Money>> {
    private final IAccountReadRepository accountReadRepository;

    public GetAccountBalanceQueryHandler(IAccountReadRepository accountReadRepository) {
        this.accountReadRepository = accountReadRepository;
    }

    @Override
    @Transactional
    public Result<Money> handle(GetAccountBalanceQuery query) {
        var optAccount = accountReadRepository
                .findSummaryById(AccountId.of(query.accountId()));
        return optAccount.map(accountSummary
                -> Result.success(accountSummary.balance())).orElseGet(()
                -> Result.failure("Account not found"));
    }
}
