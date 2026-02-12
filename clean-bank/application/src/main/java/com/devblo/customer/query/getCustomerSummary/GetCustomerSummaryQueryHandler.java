package com.devblo.customer.query.getCustomerSummary;

import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.customer.repository.ICustomerReadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCustomerSummaryQueryHandler implements IQueryHandler<GetCustomerSummaryQuery, Result<CustomerSummary>> {

    private final ICustomerReadRepository customerReadRepository;

    public GetCustomerSummaryQueryHandler(ICustomerReadRepository customerReadRepository) {
        this.customerReadRepository = customerReadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<CustomerSummary> handle(GetCustomerSummaryQuery query) {
        return customerReadRepository.findSummaryById(CustomerId.of(query.id()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
    }
}
