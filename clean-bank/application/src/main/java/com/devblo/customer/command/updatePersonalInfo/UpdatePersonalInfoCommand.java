package com.devblo.customer.command.updatePersonalInfo;

import com.devblo.common.ICommand;
import com.devblo.common.result.Result;
import com.devblo.customer.PersonalInfo;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePersonalInfoCommand(UUID customerId, String firstName, String lastName, String email,
                                        LocalDate dateOfBirth) implements ICommand<Result<PersonalInfo>> {
}
