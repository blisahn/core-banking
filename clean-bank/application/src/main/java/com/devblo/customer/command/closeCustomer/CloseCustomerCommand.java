package com.devblo.customer.command.closeCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

public record CloseCustomerCommand(String customerId) implements ICommand<Result<Void>> {
}
