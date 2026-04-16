package com.devblo.api.config;

import com.devblo.common.security.PasswordEncoderPort;
import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.repository.ICustomerWriteRepository;
import com.devblo.shared.Email;
import com.devblo.user.Role;
import com.devblo.user.User;
import com.devblo.user.repository.IUserReadRepository;
import com.devblo.user.repository.IUserWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final IUserReadRepository userReadRepository;
    private final IUserWriteRepository userWriteRepository;
    private final ICustomerWriteRepository customerWriteRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    
    @Value("${seedData.email}")
    private String adminEmail;
    
    @Value("${seedData.password}")
    private String adminPass;

    @Override
    public void run(String... args) {
        try {
            var existingAdmin = userReadRepository.findUserByEmail(adminEmail);
            if (existingAdmin.isEmpty()) {
                log.warn("Admin user not found. Seeding default admin user.");

                String encodedPassword = passwordEncoderPort.encode(adminPass);

                var personalInfo = PersonalInfo.of(
                        "System",
                        "Admin",
                        adminEmail,
                        LocalDate.of(1990, 1, 1),
                        encodedPassword
                );

                Address address = Address.of("System Street", "System District");
                var adminCustomer = Customer.register(personalInfo, address);
                customerWriteRepository.save(adminCustomer);

                var adminUser = User.create(
                        Email.of(adminEmail),
                        encodedPassword,
                        Role.ADMIN,
                        adminCustomer.getId());
                userWriteRepository.save(adminUser);

                log.warn("Admin user created successfully with email: {}", adminEmail);
            } else {
                log.warn("Admin user already exists. Skipping seed.");
            }
        } catch (Exception e) {
            log.error("AdminSeeder FAILED: {}", e.getMessage(), e);
            throw e;
        }
    }
}
