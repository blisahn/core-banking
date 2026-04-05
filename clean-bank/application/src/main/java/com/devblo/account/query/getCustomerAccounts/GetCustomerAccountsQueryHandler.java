package com.devblo.account.query.getCustomerAccounts;

import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import com.devblo.account.repository.IAccountReadRepository;
import com.devblo.account.repository.AccountSummary;
import com.devblo.customer.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCustomerAccountsQueryHandler implements IQueryHandler<GetCustomerAccountsQuery, Result<List<AccountSummary>>> {

    private final IAccountReadRepository readRepository;

    @Override
    public Result<List<AccountSummary>> handle(GetCustomerAccountsQuery query) {
        var customerId = new CustomerId(query.customerId());
        List<AccountSummary> summaries = readRepository.findSummariesByCustomerId(customerId);
        return Result.success(summaries);
    }
}
