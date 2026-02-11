package com.devblo.customer.command.suspendCustomer;

import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuspendCustomerCommandHandler implements ICommandHandler<SuspendCustomerCommand, Result<Void>> {

    private final ICustomerWriteRepository customerWriteRepository;

    public SuspendCustomerCommandHandler(ICustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(SuspendCustomerCommand command) {
        var optCustomer = customerWriteRepository
                .findById(CustomerId.of(command.customerId()));
        if (optCustomer.isEmpty()) {
            return Result.failure("Customer with id " + command.customerId() + " not found");
        }
        try {
            Customer customer = optCustomer.get();
            customer.suspend();
            customerWriteRepository.save(customer);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
