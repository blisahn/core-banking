package com.devblo.account.query.getAccaountBalance;

import com.devblo.account.AccountId;
import com.devblo.account.repository.AccountSummary;
import com.devblo.account.repository.IAccountReadRepository;
import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAccountBalanceQueryHandler implements IQueryHandler<GetAccountBalanceQuery, Result<AccountSummary>> {
    private final IAccountReadRepository accountReadRepository;

    public GetAccountBalanceQueryHandler(IAccountReadRepository accountReadRepository) {
        this.accountReadRepository = accountReadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<AccountSummary> handle(GetAccountBalanceQuery query) {
        return accountReadRepository
                .findSummaryById(AccountId.of(query.accountId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
    }
}
