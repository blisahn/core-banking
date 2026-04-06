package com.devblo.admin.query.getAllCustomers;

import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.customer.repository.ICustomerReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCustomersQueryHandler implements IQueryHandler<GetAllCustomersQuery, Result<List<CustomerSummary>>> {

    private final ICustomerReadRepository readRepository;

    @Override
    public Result<List<CustomerSummary>> handle(GetAllCustomersQuery query) {
        List<CustomerSummary> customers = readRepository.findAll();
        return Result.success(customers);
    }
}
