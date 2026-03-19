package com.devblo.customer.command.registerCustomer;


import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

public record RegisterCustomerCommand(String firstName, String lastName, String email, String dateOfBirth,
                                      String street, String district,
                                      String password) implements ICommand<Result<Void>> {
}
