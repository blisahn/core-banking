package com.devblo.customer.command.activateCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

public record ActivateCustomerCommand(String customerId) implements ICommand<Result<Void>> {
}
