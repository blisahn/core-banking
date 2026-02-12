package com.devblo.customer.command.activateCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

import java.util.UUID;

public record ActivateCustomerCommand(UUID customerId) implements ICommand<Result<Void>> {
}
