package com.devblo.customer.command.suspendCustomer;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
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
        Result<Customer> findCustomerResult = customerWriteRepository
                .findById(CustomerId.of(command.customerId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Customer not found"));
        if (findCustomerResult.isFailure()) {
            return Result.failure(findCustomerResult.getError());
        }
        Customer customer = findCustomerResult.getValue();
        Result<Void> suspendResult = customer.suspend();
        if (suspendResult.isFailure()) {
            return Result.failure(suspendResult.getError());
        }
        customerWriteRepository.save(customer);
        return Result.success();

    }
}
