package com.devblo.customer.query.getCustomerSummary;

import com.devblo.common.IQuery;
import com.devblo.common.result.Result;
import com.devblo.customer.repository.CustomerSummary;

import java.util.UUID;

public record GetCustomerSummaryQuery(UUID id) implements IQuery<Result<CustomerSummary>> {
}
