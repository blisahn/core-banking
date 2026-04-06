package com.devblo.admin.query.getAllCustomers;

import com.devblo.common.IQuery;
import com.devblo.common.result.Result;
import com.devblo.customer.repository.CustomerSummary;

import java.util.List;

public record GetAllCustomersQuery() implements IQuery<Result<List<CustomerSummary>>> {
}
