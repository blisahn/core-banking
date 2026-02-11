package com.devblo.customer.command.closeCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.Result;

import java.util.UUID;

public record CloseCustomerCommand(UUID customerId) implements ICommand<Result<Void>> {
}
