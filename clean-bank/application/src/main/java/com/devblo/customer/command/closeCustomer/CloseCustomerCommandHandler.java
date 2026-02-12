package com.devblo.customer.command.closeCustomer;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CloseCustomerCommandHandler implements ICommandHandler<CloseCustomerCommand, Result<Void>> {

    private final ICustomerWriteRepository customerWriteRepository;

    public CloseCustomerCommandHandler(ICustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(CloseCustomerCommand command) {
        Result<Customer> closeCustomerResult = customerWriteRepository
                .findById(CustomerId.of(command.customerId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Customer not found"));
        if (closeCustomerResult.isFailure()) {
            return Result.failure(closeCustomerResult.getError());
        }
        Customer customer = closeCustomerResult.getValue();
        var closeResult = customer.close();
        if (closeResult.isFailure()) {
            return Result.failure(closeResult.getError());
        }
        customerWriteRepository.save(customer);
        return Result.success();

    }
}
