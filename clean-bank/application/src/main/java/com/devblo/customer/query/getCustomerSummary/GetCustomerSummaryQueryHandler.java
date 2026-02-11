package com.devblo.customer.query.getCustomerSummary;

import com.devblo.common.IQueryHandler;
import com.devblo.common.Result;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.customer.repository.ICustomerReadRepository;
import org.springframework.stereotype.Service;

@Service
public class GetCustomerSummaryQueryHandler implements IQueryHandler<GetCustomerSummaryQuery, Result<CustomerSummary>> {

    private final ICustomerReadRepository customerReadRepository;

    public GetCustomerSummaryQueryHandler(ICustomerReadRepository customerReadRepository) {
        this.customerReadRepository = customerReadRepository;
    }

    @Override
    public Result<CustomerSummary> handle(GetCustomerSummaryQuery query) {
        var optSummary = customerReadRepository.findSummaryById(CustomerId.of(query.id()));
        return optSummary.map(Result::success).orElseGet(()
                -> Result.failure("Account not found"));
    }
}
