package com.devblo.customer.command.activateCustomer;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivateCustomerCommandHandler implements ICommandHandler<ActivateCustomerCommand, Result<Void>> {

    private final ICustomerWriteRepository customerWriteRepository;

    public ActivateCustomerCommandHandler(ICustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(ActivateCustomerCommand command) {
        Result<Customer> customerToFound = customerWriteRepository
                .findById(CustomerId.of(command.customerId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Customer not found"));
        if (customerToFound.isFailure()) {
            return Result.failure(customerToFound.getError());
        }
        var customer = customerToFound.getValue();
        Result<Void> activateResult = customer.activate();
        if (activateResult.isFailure()) {
            return Result.failure(activateResult.getError());
        }
        customerWriteRepository.save(customer);
        return Result.success();
    }
}
