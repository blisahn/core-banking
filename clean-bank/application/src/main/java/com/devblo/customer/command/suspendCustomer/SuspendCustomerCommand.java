package com.devblo.customer.command.suspendCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

import java.util.UUID;

public record SuspendCustomerCommand(UUID customerId) implements ICommand<Result<Void>> {
}
