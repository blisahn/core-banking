package com.devblo.customer.command.updateAddress;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.customer.Address;

public record UpdateAddressCommand(String customerId, String street, String district)
        implements ICommand<Result<Address>> {
}
