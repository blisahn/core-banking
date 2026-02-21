package com.devblo.customer.command.suspendCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

public record SuspendCustomerCommand(String customerId) implements ICommand<Result<Void>> {
}
