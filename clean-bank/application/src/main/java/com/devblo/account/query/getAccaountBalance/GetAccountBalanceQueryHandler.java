package com.devblo.account.query.getAccaountBalance;

import com.devblo.account.repository.IAccountReadRepository;
import com.devblo.common.IQueryHandler;
import com.devblo.common.Result;
import com.devblo.shared.Money;
import org.springframework.transaction.annotation.Transactional;

public class GetAccountBalanceQueryHandler implements IQueryHandler<GetAccountBalanceQuery, Result<Money>> {
    private final IAccountReadRepository accountReadRepository;

    public GetAccountBalanceQueryHandler(IAccountReadRepository accountReadRepository) {
        this.accountReadRepository = accountReadRepository;
    }

    @Override
    @Transactional
    public Result<Money> handle(GetAccountBalanceQuery query) {
        var optAccount = accountReadRepository
                .findSummaryById(query.accountId());
        return optAccount.map(accountSummary -> Result.ok(accountSummary.balance())).orElseGet(() -> Result.notFound("Account not found"));
    }
}
