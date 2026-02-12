package com.devblo.customer.command.registerCustomer;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.customer.CustomerId;

public record RegisterCustomerCommand(String firstName, String lastName, String email, String dateOfBirth,
                                      String street, String district) implements ICommand<Result<CustomerId>> {
}
