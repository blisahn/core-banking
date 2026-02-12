package com.devblo.customer.command.updateAddress;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.customer.Address;

import java.util.UUID;

public record UpdateAddressCommand(UUID customerId, String street, String district) implements ICommand<Result<Address>> {
}
